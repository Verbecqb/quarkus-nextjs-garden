package org.garden.planner.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.garden.planner.model.mock.ConfigPlants;

import java.time.LocalDate;
import java.util.Map;

@MongoEntity(collection = "items", database = "my_garden")
@Getter
@Setter
@Builder
public class Item extends ReactivePanacheMongoEntity {

     @Enumerated(EnumType.STRING)
     private ConfigPlants configPlants;

     private LocalDate planted_date;
     private LocalDate pickup_date;

     private RaisedBed raisedBed;
     private Map.Entry<Integer, Integer> location;

     @Enumerated(EnumType.STRING)
     @Builder.Default
     private PlantState state = PlantState.SEED;

     public void takeAction() {
          state.nextStep(this);
     }
}
