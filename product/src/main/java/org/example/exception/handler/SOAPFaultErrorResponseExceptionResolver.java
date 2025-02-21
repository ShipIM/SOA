package org.example.exception.handler;

import org.example.exception.APIException;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;

public class SOAPFaultErrorResponseExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("code");
    private static final QName MESSAGE = new QName("message");
    private static final QName FIELD = new QName("field");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof APIException) {
            var errorResponse = ((APIException) ex).getErrorResponse();
            var detail = fault.addFaultDetail();

            detail.addFaultDetailElement(CODE).addText(errorResponse.getCode() == null ? "" : errorResponse.getCode());
            detail.addFaultDetailElement(MESSAGE).addText(errorResponse.getMessage() == null ? "" : errorResponse.getMessage());
            detail.addFaultDetailElement(FIELD).addText(errorResponse.getField() == null ? "" : errorResponse.getField());
        }
    }

}