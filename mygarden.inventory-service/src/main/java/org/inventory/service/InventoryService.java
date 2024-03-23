package org.inventory.service;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.inventory.model.Inventory;
import org.inventory.model.InventoryID;
import org.inventory.order.Order;
import org.inventory.repository.InventoryRepository;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public Uni<List<Inventory>> findByProductId(UUID id, int pageIndex, int pageSize) {
        return inventoryRepository.
                <Inventory>find("inventoryID.product_id = ?1", id)
                .page(pageIndex, pageSize)
                .list();
    }

    @WithTransaction
    public Uni<Inventory> add(Inventory inventory) {
        return inventoryRepository.persist(inventory);
    }

    public Uni<List<Inventory>> allInventory(int pageIndex, int pageSize) {
        return inventoryRepository
                .<Inventory>findAll(Sort.descending("inventoryID.product_id"))
                .page(Page.of(pageIndex, pageSize)).list();
    }

    public Uni<Inventory> getInventoryByID(InventoryID id) {
        return inventoryRepository.<Inventory>findById(id);
    }

    @WithTransaction
    public Uni<Set<Inventory>> listInventoriesByUUIDs(@RestQuery("productIds") Set<UUID> productIds) {

        return inventoryRepository.find("inventoryID.product_id in ?1", productIds)
                .list()
                .invoke(products -> {
                    // Check if the inventory map contains all requested product IDs, otherwise - throw an exception
                    if (products.size() != productIds.size()) {
                        throw new IllegalArgumentException("Some requests IDs are not in our inventory");
                    }
                })
                .onItem().transform(HashSet::new);
    }


    @Incoming("order-pending-inv")
    @Outgoing("order-confirmed-inv")
    @WithTransaction
    @Acknowledgment(value = Acknowledgment.Strategy.POST_PROCESSING)
    public Uni<Order> consumePendingOrders(Order order) {
        return reserveInventory(order);
    }


    @WithTransaction
    public Uni<Order> reserveInventory(Order order) {

        // Build a map with <Key> == Product ID ordered and Value=quantity ordered
        Map<UUID, Integer> mapProductIDtoOrderedQty = new LinkedHashMap<>();
        order.orderItemList().stream().collect(Collectors.toUnmodifiableSet()).forEach(e -> {
            mapProductIDtoOrderedQty.put(e.product_id(), e.quantity());
        });

        // Fetch the list of inventories in DB
        Uni<List<Inventory>> persistedInventories = inventoryRepository.<Inventory>list("inventoryID.product_id in ?1", mapProductIDtoOrderedQty.keySet());

        return persistedInventories
                .onItem().invoke(e -> {  // Validation that the list if not empty.
                    if (e.isEmpty()) {
                        throw new WebApplicationException("None of the ordered items can be found.");
                    }
                })
                .chain(inventories -> {

                    inventories.forEach(inventory -> {   // Loop over each inventory in DB

                        // Compute the updated available qty
                        int newStockQty = inventory.getQuantity_available() - mapProductIDtoOrderedQty.get(inventory.getInventoryID().getProduct_id());

                        // If not enough stock, raise an exception.
                        if (newStockQty < 0) throw new IllegalStateException();

                        // Otherwise, update the quantity
                        inventory.setQuantity_available(newStockQty);

                    });

                    return inventoryRepository.<Inventory>persist(inventories.stream());

                })
                .chain(() -> {
                    Log.info("InventoryService#reserveInventory success");
                    return Uni.createFrom().item(order);
                })
                .onFailure().invoke(ex -> {
                            Log.error("InventoryService#reserveInventory failed with " + ex.getMessage());
                            throw new WebApplicationException(ex.getMessage());
                        }
                );

    }


}
