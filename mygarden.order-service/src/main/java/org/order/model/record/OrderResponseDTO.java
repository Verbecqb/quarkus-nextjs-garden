package org.order.model.record;

import org.order.model.Order;

import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        Long id,
        String lastname,
        String firstname,
        Order.Status status,
        List<OrderItemDTO> order_items

) {  }

