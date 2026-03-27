package com.smartcampus.resource;

import com.smartcampus.model.SensorReading;
import com.smartcampus.repository.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        return DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
    }
    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource(@PathParam("id") String id) {
        return new SensorReadingResource(id);
}
    @POST
public Response addReading(SensorReading reading) {

    if (!DataStore.sensors.containsKey(sensorId)) {
        return Response.status(404).entity("Sensor not found").build();
    }

    DataStore.readings
            .computeIfAbsent(sensorId, k -> new ArrayList<>())
            .add(reading);

    // update sensor current value
    DataStore.sensors.get(sensorId)
            .setCurrentValue(reading.getValue());

    return Response.status(Response.Status.CREATED)
            .entity(reading)
            .build();
}
}