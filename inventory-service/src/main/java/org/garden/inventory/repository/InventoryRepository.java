package org.garden.inventory.repository;


import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.garden.inventory.model.Inventory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class InventoryRepository implements PanacheRepository<Inventory> {

    public Uni<Inventory> getByProductUUIDAndWeek(UUID product_id, LocalDate date) {
        return find("product_id = ?1 and date = ?2", product_id, date.with(DayOfWeek.MONDAY)).firstResult();
    }

    public Uni<List<Inventory>> getByProductUUIDAndWeek(Map<UUID, LocalDate> mapProductQty) {
        List<Uni<Inventory>> unis = new ArrayList<Uni<Inventory>>(mapProductQty.size());
        mapProductQty.forEach(
                (key, value) -> {
                    unis.add(find("product_id = ?1 and date = ?2", key, value.with(DayOfWeek.MONDAY)).firstResult());
                }
        );
        return Uni.combine().all().unis(unis).with(Inventory.class, ArrayList::new);

    }

}
