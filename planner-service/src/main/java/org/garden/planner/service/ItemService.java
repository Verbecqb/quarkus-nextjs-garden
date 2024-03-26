package org.garden.planner.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.garden.planner.model.CellStatus;
import org.garden.planner.model.Item;
import org.garden.planner.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ApplicationScoped
public class ItemService {

    @Inject
    ItemRepository itemRepository;

    @Inject RaisedBedService raisedBedService;


    public List<int[]> getCellsRequired(int x, int y, int width_space, int length_space) {

        int startXAxis = x - width_space/2;
        int endXAxis = x + width_space/2;
        int startYAxis = y - length_space/ 2;
        int endYAxis = y + length_space/2;

        if (width_space%2 != 0) { // if width is odd
            endXAxis++;
        }
        if (length_space%2 != 0) { // if length is odd
            endYAxis++;;
        }


        List<int[]> res = new ArrayList<>();
        for (int i : IntStream.range(startXAxis, endXAxis).toArray()) {
           for (int j : IntStream.range(startYAxis, endYAxis).toArray() ) {
                res.add(new int[] {i, j});
           }
        }
        return res;
    }

    public Uni<Item> post(Item item) {

        // Call Raised Bed service to get the Array of cell status for the targeted planted date
        return raisedBedService.getCellStatus(item.getRaisedBed(), item.getPlanted_date())
                .onItem().ifNotNull().transformToUni(cells -> {

                    List<int[]> cellsRequired = getCellsRequired(
                                                    item.getLocation().get(0),
                                                    item.getLocation().get(1),
                                                    item.getConfigPlants().getSpace()[0],
                                                    item.getConfigPlants().getSpace()[1]);

                    // Check if plant location + space required fits in the raised bed
                    if (cellsRequired.stream().anyMatch(x ->
                            x[0] < 0 || x[0] >= item.getRaisedBed().getWidth() ||
                                    x[1] < 0 || x[1] >= item.getRaisedBed().getLength() )) {
                        throw new IllegalArgumentException("Plant does not fit at this location.");
                    }

                    // check if location + space required is fully empty
                    if ( cellsRequired.stream().anyMatch(x -> cells[x[0]][x[1]] != CellStatus.EMPTY)) {
                        throw new IllegalArgumentException("The location is not empty at the date of plantation planned");
                    }

                     return itemRepository.persist(item);

                })
                .onItem().ifNull().failWith(WebApplicationException::new)
                .onFailure().invoke(e -> new WebApplicationException(e.getMessage()));

    }


}
