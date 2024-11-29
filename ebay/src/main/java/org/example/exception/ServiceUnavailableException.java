package org.example.exception;

import org.example.api.exception.ApiException;

import javax.ws.rs.core.Response;

public class ServiceUnavailableException extends ApiException {

    public ServiceUnavailableException(String message) {
        super(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(), message);
    }

}
