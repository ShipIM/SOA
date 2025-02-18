package org.example.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private final int statusCode;

    public NotFoundException(String message) {
        super(message);

        this.statusCode = 404;
    }

}

