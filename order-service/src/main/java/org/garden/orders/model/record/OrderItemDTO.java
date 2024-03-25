package org.garden.orders.model.record;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record OrderItemDTO(
        @NotNull(message="Product id cannot be empty.")
        UUID product_id,

        @NotNull( message="Order date cannot be empty.")
        @FutureOrPresent( message="Order date must be today's date or future.")
        LocalDate targetPreparationDate,

        @NotNull(message="Quantity cannot be empty.")
        @Positive
        int quantity

) {}
