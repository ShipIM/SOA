package org.example.exception;

import org.example.api.exception.ApiException;

public class InternalServerErrorException extends ApiException {

    public InternalServerErrorException(String message) {
        super(500, message);
    }

}
