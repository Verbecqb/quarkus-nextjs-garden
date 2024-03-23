package org.inventory.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * Combined key for Inventory, the ID of the product and calendar week
 */
public class InventoryID {
    @NotNull(message = "Associated ProductID must be provided.")
    private UUID product_id;

    //private Integer week_of_year;

    //private Integer year;
}
