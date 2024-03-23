package org.inventory.repository;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.quarkus.test.vertx.UniAsserterInterceptor;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.inventory.model.Inventory;
import org.inventory.model.InventoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.Supplier;

@QuarkusTest
public class InventoryRepositoryTest {

    @Inject
    InventoryRepository inventoryRepository;

    static final InventoryID inventoryID = InventoryID.builder().product_id(UUID.randomUUID()).build();
    static final Inventory inventory = Inventory.builder()
            .inventoryID(inventoryID)
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

        asserter.assertThat(() -> inventoryRepository.<Inventory>persist(inventory),
                r -> {
                    asserter.putData("inventory.id", r.getInventoryID());
                }
        );

        asserter.assertEquals(() -> inventoryRepository.count(), 1L);

        asserter.assertThat(
                () -> inventoryRepository.<Inventory>findById((InventoryID)asserter.getData("inventory.id")),
                persistedEntity -> {
                    Assertions.assertNotNull(persistedEntity);
                    Assertions.assertEquals(persistedEntity.getInventoryID(), inventory.getInventoryID());
                });

    }



}
