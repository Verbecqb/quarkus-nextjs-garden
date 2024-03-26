package org.garden.planner.service;


import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;
import org.garden.planner.model.CellStatus;
import org.garden.planner.model.Item;
import org.garden.planner.model.RaisedBed;
import org.garden.planner.repository.ItemRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class RaisedBedService {

    ItemRepository itemRepository;

    ItemService itemService;

    public Uni<Map<List<Integer>, Item>> mapViewByDate(RaisedBed raisedBed, LocalDate date) {

        return itemRepository.<Item>find("id == ?1 AND planted_date >= ?2 AND pickup_date < ?2 ", raisedBed.id, date).list()
                .onItem().ifNotNull().transform(e -> e.stream().collect(Collectors.toMap(Item::getLocation, Function.identity())));

    }

    public Uni<CellStatus[][]> getCellStatus(RaisedBed raisedBed, LocalDate date) {

        CellStatus[][] res = new CellStatus[raisedBed.getLength()][raisedBed.getWidth()];
        for (CellStatus[] i : res) {
            Arrays.fill(i, CellStatus.EMPTY);
        }

        return mapViewByDate(raisedBed, date)
                .onItem().ifNotNull().transform(e -> {
                            e.forEach((pos, item) -> {

                                        List<int[]> spaceRequired = itemService.getCellsRequired(pos.get(0), pos.get(1),
                                                item.getConfigPlants().getSpace()[0], item.getConfigPlants().getSpace()[1]);

                                        for (int i : spaceRequired.get(0)) {
                                            for (int j : spaceRequired.get(1)) {
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

