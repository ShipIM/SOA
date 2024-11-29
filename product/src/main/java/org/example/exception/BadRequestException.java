package org.example.exception;

import org.example.api.exception.ApiException;

import javax.ws.rs.core.Response;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(Response.Status.BAD_REQUEST.getStatusCode(), message);
    }

}
