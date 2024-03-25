package org.garden.orders.repository;

import io.quarkus.test.junit.QuarkusTest;
import org.garden.orders.model.Order;
import org.garden.orders.model.OrderItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.UUID;

@QuarkusTest
public class OrderRepositoryTest {

    static final OrderItem orderItem1 = OrderItem.builder().product_id(UUID.randomUUID()).quantity(2).build();
    static final OrderItem orderItem2 = OrderItem.builder().product_id(UUID.randomUUID()).quantity(2).build();

    @Test
    public void createOrder_success() {


        Order order = Order.builder()
                .firstname("John").lastname("Tod")
                .orderItemList(Arrays.asList(orderItem1, orderItem2))
                .build();

        Order.persist(order);

        Assertions.assertNotNull(order.getId());
        Assertions.assertTrue(Order.find("id", order.getId()).list().await().indefinitely().contains(order));
    }
}

