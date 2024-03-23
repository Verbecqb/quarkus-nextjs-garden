package org.inventory.repository;


import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.inventory.model.Inventory;
import org.inventory.model.InventoryID;

@ApplicationScoped
public class InventoryRepository implements PanacheRepositoryBase<Inventory, InventoryID> {

}
