package org.example.exception.handler;

import org.example.exception.BadRequestException;
import org.example.exception.InternalServerErrorException;
import org.example.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
@RestControllerAdvice
public class InternalServerErrorExceptionHandler {

    @ExceptionHandler(value = InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalServerErrorException(InternalServerErrorException exception) {
        return Map.of("error", exception.getMessage());
    }

}
