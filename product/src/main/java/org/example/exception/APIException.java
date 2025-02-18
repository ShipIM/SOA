package org.example.exception;

import lombok.Getter;
import lombok.Setter;
import org.example.products.ErrorResponse;

@Getter
@Setter
public class APIException extends RuntimeException {

    private ErrorResponse errorResponse;

    public APIException(String message, ErrorResponse serviceFault) {
        super(message);
        this.errorResponse = serviceFault;
    }

    public APIException(String message, Throwable e, ErrorResponse errorResponse) {
        super(message, e);
        this.errorResponse = errorResponse;
    }

}
