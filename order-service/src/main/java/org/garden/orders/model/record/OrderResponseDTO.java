package org.garden.orders.model.record;

import org.garden.orders.model.Order;
import org.garden.orders.model.OrderState;

import java.util.List;

public record OrderResponseDTO(
        Long id,
        String lastname,
        String firstname,
        OrderState state,
        List<OrderItemDTO> order_items

) {  }

