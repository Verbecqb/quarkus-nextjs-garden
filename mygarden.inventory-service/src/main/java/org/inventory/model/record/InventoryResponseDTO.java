package org.inventory.model.record;


import org.inventory.model.Inventory;
import org.inventory.model.InventoryID;


public record InventoryResponseDTO (

    InventoryID inventoryID,

    int quantity_available,

    int forecasted_qty_available,

    Inventory.UnitQuantity unit

) { }
