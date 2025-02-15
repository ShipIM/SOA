package org.example.exception;

import jakarta.ws.rs.core.Response;
import org.example.api.exception.ApiException;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(Response.Status.BAD_REQUEST.getStatusCode(), message);
    }

}
