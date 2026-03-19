package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.repository.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Collection<Sensor> getSensors() {
        return DataStore.sensors.values();
    }

    @POST
public Response createSensor(Sensor sensor) {

    // VALIDATION: room must exist
    if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
        return Response.status(422)
                .entity("Room not found")
                .build();
    }

    DataStore.sensors.put(sensor.getId(), sensor);

    return Response.status(Response.Status.CREATED)
            .entity(sensor)
            .build();
}
}