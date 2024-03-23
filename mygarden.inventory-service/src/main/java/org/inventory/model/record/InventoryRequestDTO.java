package org.inventory.model.record;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.inventory.model.Inventory;
import org.inventory.model.InventoryID;


public record InventoryRequestDTO (
    @NotNull(message = "Please provide an Inventory ID")
    @Valid
    InventoryID inventoryID,
    @PositiveOrZero(message = "Available quantity cannot be negative")
    int quantity_available,
    @PositiveOrZero(message = "Forecasted quantity cannot be negative")
    int forecasted_qty_available,
    @NotNull
    Inventory.UnitQuantity unit

) { }
