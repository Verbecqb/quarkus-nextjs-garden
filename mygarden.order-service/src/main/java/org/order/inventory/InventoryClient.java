package org.order.inventory;

import io.quarkus.oidc.token.propagation.AccessToken;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;
import org.order.model.Order;


import java.util.Set;
import java.util.UUID;

@Path("/v0/inventory")
@RegisterRestClient(baseUri = "http://localhost:7801")
@AccessToken
public interface InventoryClient {

    @GET
    @Path("/product/{id}/week/{weekNumber}")
    Uni<RestResponse<InventoryResponseDTO>> getByID(long id, int weekNumber);

    @GET
    @Path("/products")
    Uni<Set<InventoryResponseDTO>> getProductsByUUID(@RestQuery("productIds") Set<UUID> productIds);

    @POST
    public Uni<Order> reserveInventory(@Valid Order order); // TODO change to InventoryRequestDTO

}
