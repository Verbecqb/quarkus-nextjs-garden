package org.order;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.oidc.UserInfo;
import io.quarkus.logging.Log;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;

import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.order.inventory.InventoryClient;
import org.order.model.Order;
import org.order.model.OrderState;
import org.order.model.record.OrderRequestDTO;
import org.order.model.record.OrderResponseDTO;
import org.order.services.OrderService;
import org.jboss.resteasy.reactive.RestResponse;
import org.order.model.mapper.OrderMapper;
import org.eclipse.microprofile.faulttolerance.*;

import org.order.model.validators.OrderRequestValidationGroup;

import java.time.Instant;
import java.util.List;


import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.OK;

@Path("/v0/order")
public class OrderResource {

    @RestClient
    @Inject
    InventoryClient inventoryClient;

    @Inject
    OrderService orderService;

    @Inject
    SecurityContext securityContext;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    OrderMapper orderMapper;

    @Inject
    @Channel("order-pending-inv")
    MutinyEmitter<Order> emitter;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Timeout(3000)
    @CircuitBreaker(
        failureRatio=0.5,
        successThreshold=3
    )
    public Uni<RestResponse<OrderResponseDTO>> make(
            @Valid @ConvertGroup(to = OrderRequestValidationGroup.POST.class) OrderRequestDTO orderDTO
            ) {

        Order order = orderMapper.requestDtoToNewEntity(orderDTO);
        order.setFirstname(securityContext.getUserPrincipal().getName());
        String subOidc = (((UserInfo) securityIdentity.getAttribute("userinfo")).get("sub")).toString();
        order.setCustomer_uuid(subOidc.substring(1, subOidc.length() - 1));


        return  inventoryClient.reserveInventory(order)
                .chain(() -> Panache.withTransaction(
                    order::<Order>persist
                ))
                .replaceWith(RestResponse.ResponseBuilder
                                .ok(orderMapper.entityToResponseDTO(order))
                                .status(CREATED).build())
                .onFailure().invoke(ex -> { 
                        throw new WebApplicationException(ex); 
                });

}

    @GET
    @RolesAllowed("user")
    public Uni<RestResponse<List<OrderResponseDTO>>> listOrdersByUserUUID(
            @QueryParam("pageIndex") int pageIndex,
            @QueryParam("page") int page) {

        //TODO need to find in quarkus dev
        // how to automatically set the SUB's value when keycloack starts as devservice
        String uuid = ((UserInfo) securityIdentity.getAttribute("userinfo")).get("sub").toString();
        uuid = uuid.substring(1, uuid.length()-1);

        // Get list of orders associated
        return Order.getOrdersByCustomerUUID(uuid, pageIndex, page)
                .map(orders -> orders.stream().map(orderMapper::entityToResponseDTO).toList())
                .map(l -> RestResponse.ResponseBuilder.ok(l).status(OK).build());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Uni<RestResponse<OrderResponseDTO>> putbyID(
            Long id,
            @Valid @RequestBody OrderRequestDTO orderDTO) {

        return Panache.withTransaction(() ->
                        Order.<Order> findById(id)
                                .onItem().ifNotNull().invoke(e -> orderMapper.requestDtoWithIDToEntity((orderDTO)))
                                .onItem().ifNull().failWith(NotFoundException::new)
                )
                .onItem().ifNotNull().transform(entity ->
                        RestResponse.ResponseBuilder.ok(orderMapper.entityToResponseDTO(entity)).status(OK).build()
                );

    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
   // @Incoming("reservations")
    public Uni<String> streamOrderStatus() {
            return Uni.createFrom().item("Test");
   }



}
