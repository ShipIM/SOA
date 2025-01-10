package org.example.mapper;

import org.example.exception.ApiException;

import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    @Override
    public Response toResponse(ApiException exception) {
        var message = exception.getMessage();

        var jsonObject = Json.createObjectBuilder();

        var jsonArray = Json.createArrayBuilder();

        var jsonError = Json.createObjectBuilder()
                .add("message", message)
                .build();
        jsonArray.add(jsonError);

        var errorJsonEntity = jsonObject.add("errors", jsonArray.build()).build();

        return Response.status(exception.getStatusCode())
                .entity(errorJsonEntity)
                .build();
    }

}
