package org.example.dto.mapper;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.dto.error.ErrorDetail;
import org.example.dto.error.ErrorResponse;

import java.util.List;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        var errorMessage = exception.getMessage();

        if (errorMessage != null && errorMessage.contains("RESTEASY003870")) {
            var field = "unknown field";
            var message = "invalid parameter value";

            var parts = errorMessage.split(":");
            if (parts.length > 2) {
                field = parts[2].split("\"")[1];
                var value = parts[2].split("\'")[1];
                message = "invalid value '" + value + "'";
            }

            var errorDetail = new ErrorDetail(field, message);

            var errorResponse = new ErrorResponse(List.of(errorDetail));

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        }

        var errorDetail = new ErrorDetail(null, errorMessage);
        var errorResponse = new ErrorResponse(List.of(errorDetail));

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }

}