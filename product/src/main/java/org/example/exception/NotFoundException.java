package org.example.exception;

import jakarta.ws.rs.core.Response;
import org.example.api.exception.ApiException;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(Response.Status.NOT_FOUND.getStatusCode(), message);
    }

}
