package org.garden.orders.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="t_order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends PanacheEntity {

    private UUID product_id;

    private LocalDate targetPreparationDate;

    private int quantity;

}
