package org.garden.orders.clients;


import java.time.LocalDate;
import java.util.UUID;

public record InventoryRequestDTO (

        UUID product_id,

        LocalDate date,

        int quantity_available,

        int forecasted_qty_available



) { }
