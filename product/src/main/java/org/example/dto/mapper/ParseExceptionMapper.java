package org.example.dto.mapper;

import com.fasterxml.jackson.core.JsonParseException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ParseExceptionMapper implements ExceptionMapper<JsonParseException> {

    @Override
    public Response toResponse(JsonParseException exception) {
        var errorMessage = "invalid json format";

        JsonObject errorResponse = Json.createObjectBuilder()
                .add("message", errorMessage)
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }

}

