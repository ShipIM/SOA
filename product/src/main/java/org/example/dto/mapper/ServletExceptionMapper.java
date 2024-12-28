package org.example.dto.mapper;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServletExceptionMapper implements ExceptionMapper<ServletException> {

    @Override
    public Response toResponse(ServletException exception) {
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
