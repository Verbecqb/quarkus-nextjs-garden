package org.order.inventory;

public record InventoryResponseDTO (
        InventoryID inventoryID,
        int quantity_available,
        int forecasted_qty_available

) { }
