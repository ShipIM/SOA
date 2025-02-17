package org.example.exception;

import org.example.api.exception.ApiException;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.SERVER, faultStringOrReason = "Internal Server Error")
public class InternalServerErrorException extends ApiException {

    public InternalServerErrorException(String message) {
        super(500, message);
    }

}
