package org.inventory;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.reactive.messaging.*;
import org.inventory.model.Inventory;
import org.inventory.model.mapper.InventoryMapper;
import org.inventory.model.record.InventoryRequestDTO;
import org.inventory.model.record.InventoryResponseDTO;
import org.inventory.order.Order;
import org.inventory.service.InventoryService;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;
import org.inventory.model.InventoryID;


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
    @Path("/product/{id}/week/{weekNumber}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<InventoryResponseDTO>> getByID(UUID id, int weekNumber) {

        InventoryID inventoryID = InventoryID.builder().product_id(id).build();

        return inventoryService.getInventoryByID(inventoryID)
                .onItem().ifNull().failWith(NotFoundException::new)
                .onItem().ifNotNull()
                    .transform(inventoryMapper::entityToResponseDTO)
                    .map(e -> RestResponse.ok(e).status(OK));
    }

    @GET
    @Path("/products")
    @RolesAllowed("user")
    public Uni<RestResponse<Set<InventoryResponseDTO>>> getProductsByUUID(
            @RestQuery("productIds") Set<UUID> productIds)
    {
        return inventoryService.listInventoriesByUUIDs(productIds)
                .onItem()
                .transform(
                        e -> e.stream()
                                .map(entity -> inventoryMapper.entityToResponseDTO(entity))
                                .collect(Collectors.toUnmodifiableSet())
                ).map(e -> RestResponse.ResponseBuilder.ok(e).status(OK).build());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<InventoryResponseDTO>> createInventory(@Valid InventoryRequestDTO inventory) {

        return inventoryService.<Inventory>add(inventoryMapper.requestDtoToEntity(inventory))
                .onItem()
                .transform(e -> inventoryMapper.entityToResponseDTO(e))
                .map(entity -> RestResponse.ResponseBuilder.ok(entity).status(CREATED).build());
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Order> reserveInventory(@Valid Order order) {

        return inventoryService.reserveInventory(order);

    }


}
