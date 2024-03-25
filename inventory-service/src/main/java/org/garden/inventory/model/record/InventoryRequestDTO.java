package org.garden.inventory.model.record;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.garden.inventory.model.Inventory;

import java.time.LocalDate;
import java.util.UUID;


public record InventoryRequestDTO (
        @NotNull(message = "Please provide a product ID")
        UUID product_id,

        @NotNull(message = "Please provide an Inventory date")
        @FutureOrPresent(message = "Please provide an Inventory date which is today or future")
        LocalDate date,

    @PositiveOrZero(message = "Available quantity cannot be negative")
    int quantity_available,
    @PositiveOrZero(message = "Forecasted quantity cannot be negative")
    int forecasted_qty_available,
    @NotNull
    Inventory.UnitQuantity unit

) { }
