package org.garden.planner.model;


import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.WebApplicationException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    public static class MapViewByDay {

        public enum CellStatus {EMPTY, TAKEN};

        private static Uni<Map<Map.Entry<Integer, Integer>, Item>> mapViewByDate(LocalDate date) {
            return Item.<Item>find("planted_date >= ?1 and pickup_date < ?1 ", date).list()
                    .onItem().ifNotNull().transform(e-> e.stream().collect(Collectors.toMap(Item::getLocation, Function.identity())));
        }

        public static Uni<CellStatus[][]> getCellStatus(LocalDate date, int width, int length) {

            CellStatus[][] res = new CellStatus[length][width];
            for (CellStatus[] i : res) {
                Arrays.fill(i, CellStatus.EMPTY);
            }

            return mapViewByDate(date)
                    .onItem().ifNotNull().transform( e -> {
                            e.forEach((pos,item) -> {

                                    int startXAxis = Math.abs(pos.getValue() - item.getConfigPlants().getSpace().getValue() / 2);
                                    int endXAxis = startXAxis + item.getConfigPlants().getSpace().getValue() - 1;
                                    int startYAxis = Math.abs(pos.getKey() - item.getConfigPlants().getSpace().getKey() / 2);
                                    int endYAxis = startYAxis + item.getConfigPlants().getSpace().getKey() - 1;

                                    for (int i : IntStream.rangeClosed(startXAxis, endXAxis).toArray()) {
                                        for (int j : IntStream.rangeClosed(startYAxis, endYAxis).toArray()) {
                                            res[i][j] = CellStatus.TAKEN;
                                        }
                                    }

                                }
                            );
                            return res;
                        }
                    )
                    .onItem().ifNull().failWith(WebApplicationException::new);

        }


    }

}
