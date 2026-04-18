package com.smartcampus.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    @POST
    public Response addReading(SensorReading reading) {
        System.out.println("DEBUG sensorId = " + sensorId);
        System.out.println("DEBUG sensors map = " + DataStore.sensors.keySet());
        System.out.println("DEBUG reading value = " + reading);

       
        if (sensorId == null || !DataStore.sensors.containsKey(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }

   
        List<SensorReading> sensorReadings =
                DataStore.readings.computeIfAbsent(sensorId, k -> new ArrayList<>());

        sensorReadings.add(reading);

      
        DataStore.sensors.get(sensorId)
                .setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}