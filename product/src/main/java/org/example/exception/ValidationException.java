package org.example.exception;

import org.example.api.exception.ApiException;

public class ValidationException extends ApiException {

    public ValidationException(String message) {
        super(422, message);
    }

}
