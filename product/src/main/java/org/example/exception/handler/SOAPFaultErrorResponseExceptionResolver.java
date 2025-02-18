package org.example.exception.handler;

import org.example.exception.APIException;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

public class SOAPFaultErrorResponseExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("code");
    private static final QName MESSAGE = new QName("message");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof APIException) {
            var errorResponse = ((APIException) ex).getErrorResponse();
            var detail = fault.addFaultDetail();

            detail.addFaultDetailElement(CODE).addText(errorResponse.getCode());
            detail.addFaultDetailElement(MESSAGE).addText(errorResponse.getMessage());
        }
    }

}