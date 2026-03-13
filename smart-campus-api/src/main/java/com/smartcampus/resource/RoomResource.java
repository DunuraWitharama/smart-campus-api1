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
    DataStore.rooms.put(room.getId(), room);
    return Response.status(Response.Status.CREATED).entity(room).build();
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
}