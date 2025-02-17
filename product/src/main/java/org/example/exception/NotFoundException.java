package org.example.exception;

import org.example.api.exception.ApiException;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT, faultStringOrReason = "Not Found")
public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(404, message);
    }

}
