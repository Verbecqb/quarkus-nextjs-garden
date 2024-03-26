package org.garden.planner;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.MockitoConfig;
import io.smallrye.mutiny.Uni;
import org.garden.planner.model.CellStatus;
import org.garden.planner.model.Item;
import org.garden.planner.model.RaisedBed;
import org.garden.planner.model.mock.MySinglePickupPlants;
import org.garden.planner.service.RaisedBedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class MapServiceTest {

    @InjectMock
    @MockitoConfig(convertScopes = true)
    RaisedBedService raisedBedService;

    @BeforeEach
    void setUp() {

        Uni<Map<List<Integer>, Item>> mockedMapByViewDateResult = Uni.createFrom().item(
                Map.ofEntries(
                        entry(Arrays.asList(6, 6),
                                Item.builder()
                                        .configPlants(MySinglePickupPlants.SALAD)
                                        .build()),
                        entry(Arrays.asList(0, 0),
                                Item.builder()
                                        .configPlants(MySinglePickupPlants.SPINACH)
                                        .build()),
                        entry(Arrays.asList(4, 2),
                                Item.builder()
                                        .configPlants(MySinglePickupPlants.BROCOLI)
                                        .build())
                )
        );

        when(raisedBedService.mapViewByDate(any(), any())).thenReturn(mockedMapByViewDateResult);
        when(raisedBedService.getCellStatus(any(), any())).thenCallRealMethod();
    }

    @Test
    public void raised_bed_test() {

        RaisedBed rb1 = new RaisedBed();
        rb1.setWidth(8);
        rb1.setLength(8);

        CellStatus[][] res = raisedBedService.getCellStatus(rb1, LocalDate.now()).await().indefinitely();

        int i = 0;
        int j = 0;
        for (CellStatus[] re : res) {
            System.out.println();
            for (CellStatus cellStatus : re) {
                System.out.print((cellStatus == CellStatus.EMPTY ? "O" : "X") + " ");
                if (i == 0 && j == 0) {
                    assertEquals(cellStatus, CellStatus.TAKEN);
                }
                if (i == 6 && j == 6) {
                    assertEquals(cellStatus, CellStatus.TAKEN);
                }
                if (i == 3 && j == 2) {
                    assertEquals(cellStatus, CellStatus.TAKEN);
                }

                j++;
            }
            i++;
            j = 0;
        }

    }

}

