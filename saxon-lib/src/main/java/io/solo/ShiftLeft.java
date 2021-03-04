package io.solo;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.EmptySequence;

public class ShiftLeft extends ExtensionFunctionDefinition {

    private HttpRequest httpRequest;

    public ShiftLeft(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public StructuredQName getFunctionQName() {
        return new StructuredQName("gloo", "http://www.solo.io/gloo-edge", "set-http-request-header");
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING};
    }

    @Override
    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.EMPTY_SEQUENCE;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new ExtensionFunctionCall() {
            @Override
            public Sequence call(XPathContext context, Sequence[] arguments) throws XPathException {

                String headerName = arguments[0].head().getStringValue();
                String headerValue = arguments[1].head().getStringValue();

                httpRequest.getHeader().put("Content-Type", "application/json");

                System.out.println(">>>> " + headerName + " " + headerValue);

                return EmptySequence.getInstance();
            }
        };
    }
}
