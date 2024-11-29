package org.example.filter;

import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request) {

        if (isPreflightRequest(request)) {
            request.abortWith(Response.ok().build());
        }
    }

    private static boolean isPreflightRequest(ContainerRequestContext request) {
        return request.getHeaderString("Origin") != null
                && request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) {

        if (request.getHeaderString("Origin") == null) {
            return;
        }

        if (isPreflightRequest(request)) {
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD");
            response.getHeaders().add("Access-Control-Allow-Headers",
                    "X-Requested-With, Authorization, " +
                            "Accept-Version, Content-MD5, CSRF-Token, Content-Type");
        }

        response.getHeaders().add("Access-Control-Allow-Origin", "*");
    }

}