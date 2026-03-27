package com.smartcampus.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

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

    if (sensorId == null || !DataStore.sensors.containsKey(sensorId)) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Sensor not found")
                .build();
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