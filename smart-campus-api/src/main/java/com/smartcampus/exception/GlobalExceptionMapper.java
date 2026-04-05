package com.smartcampus.exception;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.*;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        if (ex instanceof RoomNotEmptyException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(ex.getMessage())
                    .build();
        }

        if (ex instanceof LinkedResourceNotFoundException) {
            return Response.status(422)
                    .entity(ex.getMessage())
                    .build();
        }

        if (ex instanceof SensorUnavailableException) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(ex.getMessage())
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Something went wrong")
                .build();
    }
}