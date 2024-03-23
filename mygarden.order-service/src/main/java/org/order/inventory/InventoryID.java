package org.order.inventory;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryID {

    @NotNull(message = "Associated ProductID must be provided.")
    private UUID product_id;

    //private Integer week_of_year;

    //private Integer year;
}
