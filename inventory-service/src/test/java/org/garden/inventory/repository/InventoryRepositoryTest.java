package org.garden.inventory.repository;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.quarkus.test.vertx.UniAsserterInterceptor;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.garden.inventory.model.Inventory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Supplier;

@QuarkusTest
public class InventoryRepositoryTest {

    @Inject
    InventoryRepository inventoryRepository;

    static final Inventory inventory = Inventory.builder()
            .product_id(UUID.randomUUID())
            .date(LocalDate.now())
            .quantity_available(500)
            .forecasted_qty_available(10)
            .unit(Inventory.UnitQuantity.GRAMS)
            .build();

    @Test
    @RunOnVertxContext
    public void testInventoryCreate(UniAsserter uniAsserter) {

        final UniAsserter asserter = new UniAsserterInterceptor(uniAsserter) {
            @Override
            protected <T> Supplier<Uni<T>> transformUni(Supplier<Uni<T>> uniSupplier) {
                return () -> Panache.withTransaction(uniSupplier);
            }
        };


        asserter.assertEquals(() -> inventoryRepository.count(), 1L);


    }



}
