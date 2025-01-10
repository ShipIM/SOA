package org.example.mapper;

import javax.ejb.EJBException;
import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EjbExceptionMapper implements ExceptionMapper<EJBException> {

    @Override
    public Response toResponse(EJBException exception) {
        var message = exception.getMessage();

        var jsonObject = Json.createObjectBuilder();

        var jsonArray = Json.createArrayBuilder();

        var jsonError = Json.createObjectBuilder()
                .add("message", message)
                .build();
        jsonArray.add(jsonError);

        var errorJsonEntity = jsonObject.add("errors", jsonArray.build()).build();

        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorJsonEntity)
                .build();
    }

}
