package org.example.mapper;

import javax.json.Json;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        var errorMessage = exception.getMessage();

        var jsonObject = Json.createObjectBuilder();

        var jsonArray = Json.createArrayBuilder();

        if (errorMessage != null && errorMessage.contains("RESTEASY003870")) {
            var field = "unknown field";
            var message = "invalid parameter value";

            var parts = errorMessage.split(":");
            if (parts.length > 2) {
                field = parts[2].split("\"")[1];

                var value = parts[2].split("\'")[1];
                message = "invalid value '" + value + "'";
            }

            var jsonError = Json.createObjectBuilder()
                    .add("field", field)
                    .add("message", message)
                    .build();
            jsonArray.add(jsonError);

            var errorJsonEntity = jsonObject.add("errors", jsonArray.build()).build();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorJsonEntity)
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(jsonObject.add(
                        "error", errorMessage
                ))
                .build();
    }

}

