package org.garden.inventory;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.garden.inventory.model.record.InventoryRequestDTO;
import org.garden.inventory.order.Order;
import org.garden.inventory.service.InventoryService;
import org.garden.inventory.model.Inventory;
import org.garden.inventory.mappers.InventoryMapper;
import org.garden.inventory.model.record.InventoryResponseDTO;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.OK;


@Path("/v0/inventory")
@RequiredArgsConstructor
public class InventoryResource {

    private final InventoryService inventoryService;

    private final InventoryMapper inventoryMapper;

    @GET
    @Path("/product/{product_id}/week/{weekNumber}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<InventoryResponseDTO>> getByID(UUID product_id, LocalDate date) {

        return inventoryService.getInventoryByID(product_id, date)
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().ifNotNull()
                    .transform(inventoryMapper::entityToResponseDTO)
                    .map(e -> RestResponse.ok(e).status(OK));
    }

    @GET
    @Path("/products")
    @RolesAllowed("user")
    public Uni<RestResponse<Set<InventoryResponseDTO>>> getProductsByUUID(
            @NotEmpty @NotNull @RestQuery("productIds") Set<UUID> productIds)
    {
        return inventoryService.listInventoriesByUUIDs(productIds)
                .onItem()
                .transform(
                        e -> e.stream()
                                .map(inventoryMapper::entityToResponseDTO)
                                .collect(Collectors.toUnmodifiableSet())
                ).map(e -> RestResponse.ResponseBuilder.ok(e).status(OK).build());
    }

    @POST
    @Path("/inventory")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<InventoryResponseDTO>> createInventory(@Valid InventoryRequestDTO inventory) {

        return inventoryService.<Inventory>add(inventoryMapper.requestDtoToEntity(inventory))
                .onItem().ifNotNull()
                    .transform(inventoryMapper::entityToResponseDTO)
                    .map(entity -> RestResponse.ResponseBuilder.ok(entity).status(CREATED).build())
                .onFailure().invoke(ex -> new WebApplicationException("Failed to create " + inventory));
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Order> reserveInventory(Order order) {
        Log.info("Calling POST inventory on Order: " + order);
        return inventoryService.reserveInventory(order);

    }


}
