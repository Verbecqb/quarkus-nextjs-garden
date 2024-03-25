package org.garden.inventory.order;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record OrderItem (
        @NotNull
        UUID product_id,

        @FutureOrPresent
        @NotNull
        LocalDate targetPreparationDate,

        @Positive
        int quantity

) { }
