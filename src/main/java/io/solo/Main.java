package io.solo;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.stream.StreamSource;

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

    public static void main(String... args) throws SaxonApiException {
        System.out.println(transform(args[0], args[1]));
    }

    @CEntryPoint(name = "transform")
    public static CCharPointer transform(IsolateThread thread, CCharPointer xml, CCharPointer xslt) {
        try {
            String result = transform(CTypeConversion.toJavaString(xml), CTypeConversion.toJavaString(xslt));
            final CTypeConversion.CCharPointerHolder holder=CTypeConversion.toCString(result);
            return holder.get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return CTypeConversion.toCString(e.getMessage()).get();
        }
    }


    private static String transform(String xml, String xslt) throws SaxonApiException {
        Processor processor = new Processor(false);
        XsltCompiler compiler = processor.newXsltCompiler();
        XsltExecutable stylesheet = compiler.compile(new StreamSource(new StringReader(xslt)));
        var stringWriter = new StringWriter();
        Serializer out = processor.newSerializer(stringWriter);
        Xslt30Transformer transformer = stylesheet.load30();
        transformer.transform(new StreamSource(new StringReader(xml)), out);
        return stringWriter.toString();
    }
}
