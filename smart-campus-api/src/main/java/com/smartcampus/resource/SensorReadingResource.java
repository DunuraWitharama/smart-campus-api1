package com.smartcampus.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.model.SensorReading;
import com.smartcampus.repository.DataStore;

@Produces(MediaType.APPLICATION_JSON)   
@Consumes(MediaType.APPLICATION_JSON)   
public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        return DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    public Response addReading(SensorReading reading) {

        // check sensor exists
        if (sensorId == null || !DataStore.sensors.containsKey(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found"))
                    .build();
        }

        // initialize list if not exists
        List<SensorReading> sensorReadings =
                DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>());

        sensorReadings.add(reading);

        // update current value
        DataStore.sensors.get(sensorId)
                .setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}