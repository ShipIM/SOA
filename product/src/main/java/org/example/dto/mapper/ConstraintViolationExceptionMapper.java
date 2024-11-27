package org.example.dto.mapper;

import javax.json.Json;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        var constraintViolations = exception.getConstraintViolations();

        var jsonObject = Json.createObjectBuilder();

        var jsonArray = Json.createArrayBuilder();

        for (var constraint : constraintViolations) {
            var message = constraint.getMessage();
            var propertyPath = constraint.getPropertyPath().toString().split("\\.")[2];

            var jsonError = Json.createObjectBuilder()
                    .add("field", propertyPath)
                    .add("message", message)
                    .build();
            jsonArray.add(jsonError);
        }

        var errorJsonEntity = jsonObject.add("errors", jsonArray.build()).build();

        return Response.status(Response.Status.BAD_REQUEST).entity(errorJsonEntity).build();
    }

}
