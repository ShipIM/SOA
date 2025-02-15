package org.example.dto.mapper;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.dto.error.ErrorDetail;
import org.example.dto.error.ErrorResponse;

import java.util.List;

@Provider
public class InvalidFormatExceptionMapper implements ExceptionMapper<InvalidFormatException> {

    @Override
    public Response toResponse(InvalidFormatException exception) {
        var fieldName = exception.getPath() != null && !exception.getPath().isEmpty()
                ? exception.getPath().get(0).getFieldName()
                : "unknown field";

        var targetType = exception.getTargetType().getSimpleName();
        var invalidValue = exception.getValue();

        var message = String.format("invalid value '%s', expected type: %s.", invalidValue, targetType);

        var errorDetail = new ErrorDetail(fieldName, message);

        var errorResponse = new ErrorResponse(List.of(errorDetail));

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }

}