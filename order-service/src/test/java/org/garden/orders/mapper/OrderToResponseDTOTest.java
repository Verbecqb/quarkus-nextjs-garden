package org.garden.orders.mapper;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.garden.orders.model.Order;
import org.garden.orders.model.OrderItem;
import org.garden.orders.model.OrderState;
import org.garden.orders.model.record.OrderItemDTO;
import org.garden.orders.model.record.OrderResponseDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


import java.util.Arrays;
import java.util.UUID;

@QuarkusTest
public class OrderToResponseDTOTest {

    @Inject
    OrderMapper orderMapper;

    @Test
    public void order_to_dto() {

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        OrderItem orderItem1 = OrderItem.builder().id(1L).product_id(uuid1).quantity(2).build();
        OrderItem orderItem2 = OrderItem.builder().id(2L).product_id(uuid2).quantity(1).build();

        Order order = Order.builder()
                .id(121221L)
                .customer_uuid("aasas-bvcdd")
                .state(OrderState.CONFIRMED)
                .firstname("John").lastname("Tod")
                .orderItemList(Arrays.asList(orderItem1, orderItem2))
                .build();

        OrderResponseDTO orderResDTO = orderMapper.entityToResponseDTO(order);

        Assertions.assertNotNull(orderResDTO.id());
        Assertions.assertNotNull(orderResDTO.state());
        Assertions.assertFalse(orderResDTO.order_items().isEmpty());


    }
}
