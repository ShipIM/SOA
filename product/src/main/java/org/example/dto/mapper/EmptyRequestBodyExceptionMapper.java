package org.example.dto.mapper;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EmptyRequestBodyExceptionMapper implements ExceptionMapper<NullPointerException> {

    @Override
    public Response toResponse(NullPointerException exception) {
        var jsonObject = Json.createObjectBuilder();

        var jsonArray = Json.createArrayBuilder();

        var errorMessage = "empty request body";

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