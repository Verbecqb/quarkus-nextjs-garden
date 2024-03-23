package org.inventory.order;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record Order (

        @NotEmpty
        Long id,

        @NotEmpty
        List<OrderItem> orderItemList
) { }
