package org.example.dto.mapper;

import com.fasterxml.jackson.core.JsonParseException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.dto.error.ErrorDetail;
import org.example.dto.error.ErrorResponse;

import java.util.List;

@Provider
public class ParseExceptionMapper implements ExceptionMapper<JsonParseException> {

    @Override
    public Response toResponse(JsonParseException exception) {
        var errorMessage = "invalid json format";

        var errorDetail = new ErrorDetail(null, errorMessage);

        var errorResponse = new ErrorResponse(List.of(errorDetail));

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }

}