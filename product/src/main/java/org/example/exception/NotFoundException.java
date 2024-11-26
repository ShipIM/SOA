package org.example.exception;

import org.example.api.exception.ApiException;

import javax.ws.rs.core.Response;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(Response.Status.NOT_FOUND.getStatusCode(), message);
    }

}
