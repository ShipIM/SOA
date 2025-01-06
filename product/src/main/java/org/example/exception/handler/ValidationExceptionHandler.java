package org.example.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException exception) {
        Map<String, List<String>> body = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (ObjectError objectError : exception.getBindingResult().getAllErrors()) {
            String s = objectError.getDefaultMessage();
            if (s != null) {
                if (!s.isBlank()) {
                    list.add(s);
                }
            }
        }
        body.put("errors", list);

        return body;
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String>  handleConstraintViolationException
            (ConstraintViolationException exception) {
        return Map.of("error", exception.getMessage());
    }
}