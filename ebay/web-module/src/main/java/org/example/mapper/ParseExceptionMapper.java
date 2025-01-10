package org.example.mapper;

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
        var jsonObject = Json.createObjectBuilder();

        var jsonArray = Json.createArrayBuilder();

        var errorMessage = "invalid json format";

        JsonObject errorResponse = Json.createObjectBuilder()
                .add("message", errorMessage)
                .build();
        jsonArray.add(errorResponse);

        var errorJsonEntity = jsonObject.add("errors", jsonArray.build()).build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorJsonEntity)
                .build();
    }

}

