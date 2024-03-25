package org.garden.inventory.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;

public record Order(

        Long id,
        @NotEmpty
        @Valid
        List<OrderItem> orderItemList
) { }
