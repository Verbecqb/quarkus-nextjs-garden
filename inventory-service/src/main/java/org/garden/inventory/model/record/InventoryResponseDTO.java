package org.garden.inventory.model.record;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.garden.inventory.model.Inventory;

import java.time.LocalDate;
import java.util.UUID;


public record InventoryResponseDTO (

        UUID product_id,

        LocalDate date,

    int quantity_available,

    int forecasted_qty_available,

    Inventory.UnitQuantity unit

) { }
