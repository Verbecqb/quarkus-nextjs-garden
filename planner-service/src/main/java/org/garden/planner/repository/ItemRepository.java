package org.garden.planner.repository;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.garden.planner.model.Item;

@ApplicationScoped
public class ItemRepository implements ReactivePanacheMongoRepository<Item> {

}
