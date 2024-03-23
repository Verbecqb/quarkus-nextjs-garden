package org.inventory.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "t_inventory")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory extends PanacheEntityBase {

    @EmbeddedId
    private InventoryID inventoryID;

    private int quantity_available;

    private int forecasted_qty_available;

    private UnitQuantity unit;

    public enum UnitQuantity {
        GRAMS,
        KG,
        UNIT
    }

}
