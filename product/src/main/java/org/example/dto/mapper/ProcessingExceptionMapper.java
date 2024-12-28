package org.example.dto.mapper;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {

    @Override
    public Response toResponse(ProcessingException exception) {
        var jsonObject = Json.createObjectBuilder();

        var jsonArray = Json.createArrayBuilder();

        var errorMessage = "invalid format: " + exception.getMessage();

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
