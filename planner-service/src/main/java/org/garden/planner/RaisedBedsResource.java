package org.garden.planner;

import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.garden.planner.model.RaisedBed;

import java.util.List;

@Path("/v0/raisedbeds")
public class RaisedBedsResource {

    @GET
    public Uni<List<RaisedBed>> getAll() {
        return RaisedBed.<RaisedBed>listAll();
    }


    @POST
    public Uni<RaisedBed> post(@NotNull RaisedBed raisedBed) {
        return RaisedBed.<RaisedBed>persist(raisedBed).replaceWith(raisedBed);
    }
}
