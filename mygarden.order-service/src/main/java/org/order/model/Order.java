package org.order.model;

import io.smallrye.mutiny.Uni;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import lombok.*;


import java.time.Instant;
import java.util.*;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "t_orders")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends PanacheEntity {

    private Long id;

    private String customer_uuid;

    private String lastname;

    private String firstname;

    private Instant createdAt;

    private OrderState state;

    @Version
    private int version;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItemList;




    @WithTransaction
    public static Uni<List<Order>> getOrdersByCustomerUUID(String uuid, int pageIndex, int pageSize) {

        return Order.<Order>find("FROM Order order " +
                                "LEFT JOIN fetch order.orderItemList oi " +
                                "WHERE order.customer_uuid = ?1",
                                Sort.descending("createdAt"), uuid)
                    .page(Page.of(pageIndex, pageSize))
                    .list();

    }


}
