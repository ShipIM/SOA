package org.example.dto.mapper;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.dto.error.ErrorDetail;
import org.example.dto.error.ErrorResponse;

import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        var errorDetails = exception.getConstraintViolations()
                .stream()
                .map(constraint -> new ErrorDetail(
                        extractFieldName(constraint.getPropertyPath().toString()),
                        constraint.getMessage()
                ))
                .collect(Collectors.toList());

        var errorResponse = new ErrorResponse(errorDetails);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }

    private String extractFieldName(String propertyPath) {
        var parts = propertyPath.split("\\.");
        return parts.length > 2 ? parts[2] : propertyPath;
    }

}