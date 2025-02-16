package org.example.handler;

import org.example.api.exception.ApiException;
import org.example.product.ErrorDetails;
import org.example.product.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException exception) {
        var errorDetail = new ErrorDetails();
        errorDetail.setField(null);
        errorDetail.setMessage(exception.getMessage());

        var errorResponse = new ErrorResponse();
        errorResponse.getErrors().add(errorDetail);

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        var errorDetail = new ErrorDetails();
        errorDetail.setField(null);
        errorDetail.setMessage("an unexpected error occurred: " + exception.getMessage());

        var errorResponse = new ErrorResponse();
        errorResponse.getErrors().add(errorDetail);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

}