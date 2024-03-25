package org.garden.orders.clients;

public record InventoryResponseDTO (
        int quantity_available,
        int forecasted_qty_available

) { }
