package com.smartcampus.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Sensor;
import com.smartcampus.repository.DataStore;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

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

    @POST
    public Response createSensor(Sensor sensor) {

        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room not found");
        }

        DataStore.sensors.put(sensor.getId(), sensor);

        // LINK sensor to room
        DataStore.rooms.get(sensor.getRoomId())
                .getSensorIds()
                .add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(Map.of("message", "Sensor created", "data", sensor))
                .build();
    }
}