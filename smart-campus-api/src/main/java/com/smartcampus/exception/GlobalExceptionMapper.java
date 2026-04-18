package com.smartcampus.exception;

import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        // 409
        if (ex instanceof RoomNotEmptyException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", ex.getMessage()))
                    .build();
        }

        // 422
        if (ex instanceof LinkedResourceNotFoundException) {
            return Response.status(422)
                    .entity(Map.of("error", ex.getMessage()))
                    .build();
        }

        // 403
        if (ex instanceof SensorUnavailableException) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", ex.getMessage()))
                    .build();
        }

        // 500
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "Something went wrong"))
                .build();
    }
}