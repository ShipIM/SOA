package org.example.exception;

import jakarta.ws.rs.core.Response;
import org.example.api.exception.ApiException;

public class InternalServerErrorException extends ApiException {

    public InternalServerErrorException(String message) {
        super(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), message);
    }

}
