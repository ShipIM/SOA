package org.example.dto.mapper;

import org.example.api.exception.ApiException;
import org.example.dto.error.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    @Override
    public Response toResponse(ApiException exception) {
        var errorResponse = new ErrorResponse(exception.getMessage());

        return Response.status(exception.getStatusCode())
                .entity(errorResponse)
                .build();
    }

}
