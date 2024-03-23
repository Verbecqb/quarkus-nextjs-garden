package mapper;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.order.model.Order;
import org.order.model.OrderItem;
import org.order.model.mapper.OrderMapper;
import org.order.model.record.OrderItemDTO;
import org.order.model.record.OrderResponseDTO;


import java.util.Arrays;
import java.util.UUID;

@QuarkusTest
public class OrderToResponseDTO {

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
                .status(Order.Status.CONFIRMED)
                .firstname("John").lastname("Tod")
                .orderItemList(Arrays.asList(orderItem1, orderItem2))
                .build();

        OrderResponseDTO orderResDTO = orderMapper.entityToResponseDTO(order);

        Assertions.assertNotNull(orderResDTO.id());
        Assertions.assertNotNull(orderResDTO.status());
        Assertions.assertFalse(orderResDTO.order_items().isEmpty());
        Assertions.assertInstanceOf(OrderItemDTO.class, orderResDTO.order_items());

    }
}
