package org.garden.inventory.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(
        name = "t_inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique product_id, week_of_year and year",
                        columnNames = { "product_id", "date"}
                )
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID product_id;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private int quantity_available;

    private int forecasted_qty_available;

    private UnitQuantity unit;

    public enum UnitQuantity {
        GRAMS,
        KG,
        UNIT
    }

}
