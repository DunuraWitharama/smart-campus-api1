package com.smartcampus.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.SensorReading;
import com.smartcampus.repository.DataStore;

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
        return Response.status(404)
        .entity(Map.of("error", "Sensor not found"))
        .build();
    }

    // MAINTENANCE RULE
    if ("MAINTENANCE".equalsIgnoreCase(
            DataStore.sensors.get(sensorId).getStatus())) {

        throw new SensorUnavailableException("Sensor is under maintenance");
    }

    DataStore.readings
            .computeIfAbsent(sensorId, k -> new ArrayList<>())
            .add(reading);

    DataStore.sensors.get(sensorId)
            .setCurrentValue(reading.getValue());

    return Response.status(Response.Status.CREATED)
        .entity(Map.of("message", "Reading added", "data", reading))
        .build();
}
}