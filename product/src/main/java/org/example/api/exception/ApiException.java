package org.example.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class ApiException extends RuntimeException {

    private final int statusCode;

}
