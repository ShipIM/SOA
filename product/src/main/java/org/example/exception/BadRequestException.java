package org.example.exception;

import org.example.api.exception.ApiException;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(400, message);
    }

}
