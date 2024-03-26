package org.garden.planner.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import io.quarkus.mongodb.panache.reactive.runtime.JavaReactiveMongoOperations;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.garden.planner.model.mock.ConfigPlants;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@MongoEntity(collection = "items", database = "my_garden")
@Getter
@Setter
@Builder
public class Item  {

     public ObjectId id;

     @Enumerated(EnumType.STRING)
     private ConfigPlants configPlants;

     private LocalDate planted_date;
     private LocalDate pickup_date;

     private RaisedBed raisedBed;
     private List<Integer> location;

     @Enumerated(EnumType.STRING)
     private PlantState state;

     public void takeAction() {
          state.nextStep(this);
     }


}
