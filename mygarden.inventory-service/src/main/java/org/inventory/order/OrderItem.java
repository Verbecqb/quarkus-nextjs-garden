package org.inventory.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record OrderItem (
        @NotBlank
        UUID product_id,

        @Positive
        int quantity

) { }
