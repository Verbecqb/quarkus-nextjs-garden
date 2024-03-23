package org.order.inventory;


public record InventoryRequestDTO (

        InventoryID inventoryID,

        int quantity_available,

        int forecasted_qty_available



) { }
