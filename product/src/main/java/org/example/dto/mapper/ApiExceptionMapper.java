package org.example.dto.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.api.exception.ApiException;
import org.example.dto.error.ErrorDetail;
import org.example.dto.error.ErrorResponse;

import java.util.List;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    @Override
    public Response toResponse(ApiException exception) {
        var errorDetail = new ErrorDetail(
                null,
                exception.getMessage()
        );

        var errorResponse = new ErrorResponse(List.of(errorDetail));

        return Response.status(exception.getStatusCode())
                .entity(errorResponse)
                .build();
    }

}