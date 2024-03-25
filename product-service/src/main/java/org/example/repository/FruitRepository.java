package org.example.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.Fruit;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class FruitRepository implements PanacheMongoRepository<Fruit> {

    public Optional<Fruit> findByName(String name) {
        return find("name", name).stream().findFirst();
    }
    public List<Fruit> findByNameLike(String name, int pageIndex, int pageSize) {
        PanacheQuery<Fruit> fruits = find("name like ?1 ", name);
        return fruits.page(Page.of(pageIndex, pageSize)).list();
    }

}
