package org.example.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private final int statusCode;

    public BadRequestException(String message) {
        super(message);

        this.statusCode = 400;
    }

}
