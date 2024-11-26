package org.example.exception;

import org.example.api.exception.ApiException;

import javax.ws.rs.core.Response;

public class ConflictException extends ApiException {

    public ConflictException(String message) {
        super(Response.Status.CONFLICT.getStatusCode(), message);
    }

}
