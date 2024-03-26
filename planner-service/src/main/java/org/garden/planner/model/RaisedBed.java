package org.garden.planner.model;


import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.WebApplicationException;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@MongoEntity(collection = "raised_beds", database = "my_garden")
@Getter
@Setter
public class RaisedBed extends ReactivePanacheMongoEntity {

    private String name;
    private Integer width;
    private Integer length;


}
