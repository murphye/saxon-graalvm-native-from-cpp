package io.solo;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamSource;

import com.example.tutorial.AddressBookProtos.AddressBook;
import com.google.protobuf.ByteString;

import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

import io.quarkus.runtime.annotations.QuarkusMain;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;

@QuarkusMain
class Main {

    public static void main(String... args) {}

    @CEntryPoint(name = "transformSimple")
    public static CCharPointer transform(IsolateThread thread, CCharPointer xml, CCharPointer xslt) {
        try {
            String result = transform(CTypeConversion.toJavaString(xml), CTypeConversion.toJavaString(xslt), null);
            final CTypeConversion.CCharPointerHolder holder=CTypeConversion.toCString(result);
            return holder.get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return CTypeConversion.toCString(e.getMessage()).get();
        }
    }

    @CEntryPoint(name = "transformComplex")
    public static CCharPointer transform(IsolateThread thread, CCharPointer xml, CCharPointer xslt, CCharPointer objStr) {
        try {
            String objStrObj = CTypeConversion.toJavaString(objStr);
            ByteString bs = ByteString.copyFromUtf8(objStrObj);
            AddressBook addressBook = AddressBook.parseFrom(bs);

            System.out.println(">>> " + addressBook.getPeople(0).getName());

            var httpRequest = (new HttpRequest());
            httpRequest.getHeader().put("Content-Type", "text/xml");

            String result = transform(CTypeConversion.toJavaString(xml), CTypeConversion.toJavaString(xslt), httpRequest);

            final CTypeConversion.CCharPointerHolder holder=CTypeConversion.toCString(result);
            return holder.get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return CTypeConversion.toCString(e.getMessage()).get();
        }
    }



    private static String transform(String xml, String xslt, HttpRequest httpRequest) throws SaxonApiException {

        // TODO: For complex transforms, we need to make a new Processor per request
        // For simple transforms, we can shared a common Processor.


        Processor processor = new Processor(false);

        processor.registerExtensionFunction(new ShiftLeft(httpRequest));
        XsltCompiler compiler = processor.newXsltCompiler();
        XsltExecutable stylesheet = compiler.compile(new StreamSource(new StringReader(xslt)));

        // TODO: Can we cache the Stylesheet?
        Xslt30Transformer transformer = stylesheet.load30();

        var stringWriter = new StringWriter();
        Serializer out = processor.newSerializer(stringWriter);
        transformer.transform(new StreamSource(new StringReader(xml)), out);

        return stringWriter.toString();
    }
}
