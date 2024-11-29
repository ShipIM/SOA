package org.example.dto.mapper;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidFormatExceptionMapper implements ExceptionMapper<InvalidFormatException> {

    @Override
    public Response toResponse(InvalidFormatException exception) {
        var jsonObject = Json.createObjectBuilder();

        var jsonArray = Json.createArrayBuilder();

        var fieldName = exception.getPath() != null && !exception.getPath().isEmpty()
                ? exception.getPath().get(0).getFieldName()
                : "unknown field";

        var targetType = exception.getTargetType().getSimpleName();
        var invalidValue = exception.getValue();

        var message = String.format("invalid value '%s', expected type: %s.", invalidValue, targetType);

        JsonObject errorResponse = Json.createObjectBuilder()
                .add("field", fieldName)
                .add("message", message)
                .build();
        jsonArray.add(errorResponse);

        var errorJsonEntity = jsonObject.add("errors", jsonArray.build()).build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorJsonEntity)
                .build();
    }

}
