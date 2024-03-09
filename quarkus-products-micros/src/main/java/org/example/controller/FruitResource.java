package org.example.controller;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.example.model.Fruit;
import org.example.service.FruitService;
import org.jboss.resteasy.reactive.*;

import javax.swing.text.html.Option;

@Path("/fruits")
@ApplicationScoped
public class FruitResource {

    @Inject
    FruitService fruitService;


    @GET
    public RestResponse<List<Fruit>> getFruits()  {
        return RestResponse.ResponseBuilder.ok(fruitService.getFruits()).build();
    }

    @GET
    @Path("/{name}")
    public RestResponse<Fruit> getFruitByName(@RestPath String name) {

        Optional<Fruit> fruit = fruitService.findByName("name");

        if (fruit.isPresent()) {
            return RestResponse.ResponseBuilder.ok(fruit.get()).build();
        }

        throw new NotFoundException();
    }

    @GET
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    @Path("/stream/{count}")
    public Multi<String> getFruitsStream(int count) {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onItem()
                .transform(e -> "hello")
                .select()
                .first(count);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(200)
    public RestResponse<List<Fruit>> postFruit(Fruit fruit, @Context UriInfo uriInfo) throws Exception {

        // Update fruit ID and Creation time
        if (fruit.getId() != null) {
            throw new Exception("New element should not have any ID.");
        }

        fruit.setCreatedAt(Instant.now());
        fruitService.putFruit(fruit);

        return RestResponse.ResponseBuilder.ok(fruitService.getFruits()).build();

        // Redirect to the GET /{name} method
        //URI creationURI = uriInfo.getAbsolutePathBuilder().path(fruit.getName()).build();
        //return RestResponse.seeOther(creationURI);
    }

    @PUT
    public RestResponse<Fruit> putFruit(Fruit fruit) {

        fruitService.putFruit(fruit);

        return RestResponse.ResponseBuilder.ok(fruit).status(204).build();

    }

    @DELETE
    public RestResponse deleteFruit(Fruit fruit) {
        fruitService.deleteFruit(fruit);
        return RestResponse.ResponseBuilder.ok().build();
    }

}
