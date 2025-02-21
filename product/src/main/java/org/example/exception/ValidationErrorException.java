package org.example.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationErrorException extends RuntimeException {

    private final int statusCode;
    private final String field;

    public ValidationErrorException(String message, String field) {
        super(message);

        this.statusCode = 422;
        this.field = field;
    }

}
