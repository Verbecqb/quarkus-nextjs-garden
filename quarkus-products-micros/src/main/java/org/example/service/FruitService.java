package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import org.example.model.Fruit;
import org.example.repository.FruitRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class FruitService {

    @Inject
    FruitRepository fruitRepository;

    public List<Fruit> getFruits() {
        return fruitRepository.listAll();
    }

    public Optional<Fruit> findByName(String name) {
        return fruitRepository.findByName(name);
    }

    public Set<Fruit> getFruitsByNameLike(String searchPattern, int pageIndex, int pageSize) {
        return Set.copyOf(fruitRepository.findByNameLike(searchPattern, pageIndex, pageSize));
    }

    public void putFruit(Fruit fruit)  {
        // Persist or update
         fruitRepository.persistOrUpdate(fruit);
    }


    public void deleteFruit(Fruit fruit) {
        fruitRepository.delete(fruit);
    }

}
