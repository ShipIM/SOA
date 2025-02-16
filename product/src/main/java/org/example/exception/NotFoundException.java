package org.example.exception;

import org.example.api.exception.ApiException;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(404, message);
    }

}
