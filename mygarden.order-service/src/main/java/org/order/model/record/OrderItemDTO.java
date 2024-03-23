package org.order.model.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record OrderItemDTO(
        @NotBlank(message="Product id cannot be empty.")
        UUID product_id,

        @NotBlank(message="Quantity cannot be empty.")
        @PositiveOrZero
        int quantity

) {}
