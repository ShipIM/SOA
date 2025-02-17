package org.example.exception;

import org.example.api.exception.ApiException;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT, faultStringOrReason = "Bad Request")
public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(400, message);
    }

}
