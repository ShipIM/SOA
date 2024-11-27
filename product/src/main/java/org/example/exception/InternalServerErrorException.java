package org.example.exception;

import org.example.api.exception.ApiException;

import javax.ws.rs.core.Response;

public class InternalServerErrorException extends ApiException {

    public InternalServerErrorException() {
        super(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

}
