package org.garden.orders.clients;

import io.quarkus.oidc.token.propagation.AccessToken;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;
import org.garden.orders.model.Order;


import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Path("/v0/inventory")
@RegisterRestClient(baseUri = "http://localhost:7801")
@AccessToken
public interface InventoryClient {

    @GET
    @Path("/product/{product_id}/week/{weekNumber}")
    Uni<RestResponse<InventoryResponseDTO>> getByID(UUID product_id, LocalDate date);

    @GET
    @Path("/products")
    Uni<Set<InventoryResponseDTO>> getProductsByUUID(@RestQuery("productIds") Set<UUID> productIds);

    @POST
    Uni<Order> reserveInventory(Order order); // TODO change to InventoryRequestDTO

}
