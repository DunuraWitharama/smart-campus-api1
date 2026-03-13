package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.repository.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Collection<Room> getRooms() {
        return DataStore.rooms.values();
    }
    @POST
public Response createRoom(Room room) {

    if (room.getId() == null || room.getName() == null) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid room data")
                .build();
    }

    if (DataStore.rooms.containsKey(room.getId())) {
        return Response.status(Response.Status.CONFLICT)
                .entity("Room already exists")
                .build();
    }

    DataStore.rooms.put(room.getId(), room);

    return Response.status(Response.Status.CREATED)
            .entity(room)
            .build();
}
    @GET
    @Path("/{id}")
    public Response getRoom(@PathParam("id") String id) {

    Room room = DataStore.rooms.get(id);

    if (room == null) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Room not found")
                .build();
    }

    return Response.ok(room).build();
    }
    @DELETE
@Path("/{id}")
public Response deleteRoom(@PathParam("id") String id) {

    Room room = DataStore.rooms.get(id);

    if (room == null) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Room not found")
                .build();
    }

    // IMPORTANT RULE
    if (!room.getSensorIds().isEmpty()) {
        return Response.status(Response.Status.CONFLICT)
                .entity("Cannot delete room with sensors")
                .build();
    }

    DataStore.rooms.remove(id);

    return Response.ok("Room deleted successfully").build();
}
}