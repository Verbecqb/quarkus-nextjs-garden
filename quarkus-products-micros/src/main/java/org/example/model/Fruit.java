package org.example.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.Instant;

@MongoEntity(collection = "Fruits", database = "my_garden")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fruit {

    private ObjectId id;
    private String name;
    private String description;
    private Instant createdAt;

}
