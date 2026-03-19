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

    if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
        return Response.status(422)
                .entity("Room not found")
                .build();
    }

    DataStore.sensors.put(sensor.getId(), sensor);

    // LINK sensor to room
    DataStore.rooms.get(sensor.getRoomId())
            .getSensorIds()
            .add(sensor.getId());

    return Response.status(Response.Status.CREATED)
            .entity(sensor)
            .build();
}
    @GET
public Collection<Sensor> getSensors(@QueryParam("type") String type) {

    if (type == null) {
        return DataStore.sensors.values();
    }

    List<Sensor> filtered = new ArrayList<>();

    for (Sensor s : DataStore.sensors.values()) {
        if (s.getType().equalsIgnoreCase(type)) {
            filtered.add(s);
        }
    }

    return filtered;
}
}