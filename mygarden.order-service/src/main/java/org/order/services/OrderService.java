package org.order.services;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.order.inventory.InventoryClient;
import org.order.model.Order;
import org.order.model.OrderState;

@ApplicationScoped
@Slf4j
public class OrderService {

    @RestClient
    @Inject
    InventoryClient inventoryClient;


    @WithTransaction
    public static Uni<Order> approveOrder(long orderId) {
        return Order.<Order>findById(orderId)
                .onItem().invoke(e -> e.setState(OrderState.CONFIRMED))
                .onItem().ifNull().failWith(new NotFoundException());
    }

    @WithTransaction
    public static Uni<Order> rejectOrder(long orderId) {
        return Order.<Order>findById(orderId)
                .onItem().invoke(e -> e.setState(OrderState.REJECTED))
                .onItem().ifNull().failWith(new NotFoundException());
    }




}
