package org.example.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalServerErrorException extends RuntimeException {

    private final int statusCode;

    public InternalServerErrorException(String message) {
        super(message);

        this.statusCode = 500;
    }

}
