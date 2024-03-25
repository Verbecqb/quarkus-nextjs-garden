package org.garden.planner;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.garden.planner.model.Item;
import org.garden.planner.model.RaisedBed;
import org.garden.planner.model.mock.MySinglePickupPlants;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class RaisedBedTest {


    @Test
    public void raised_bed_test() {

        RaisedBed rb1 = new RaisedBed();
        rb1.setWidth(8);
        rb1.setLength(8);

        Uni<Map<Map.Entry<Integer, Integer>, Item>> mockedResponse = Uni.createFrom().item(
                Map.ofEntries(
                        entry(new AbstractMap.SimpleEntry<>(6, 6),
                                Item.builder()
                                        .configPlants(MySinglePickupPlants.SALAD)
                                        .build()),
                        entry(new AbstractMap.SimpleEntry<>(0, 0),
                                Item.builder()
                                        .configPlants(MySinglePickupPlants.SPINACH)
                                        .build()),
                        entry(new AbstractMap.SimpleEntry<>(4, 2),
                                Item.builder()
                                        .configPlants(MySinglePickupPlants.BROCOLI)
                                        .build())
                )
        );

        try (MockedStatic<RaisedBed.MapViewByDay> utils = Mockito.mockStatic(RaisedBed.MapViewByDay.class, invocation -> {
            Method method = invocation.getMethod();

            if ("getCellStatus".equals(method.getName())) {
                return invocation.callRealMethod();
            } else {
                return mockedResponse;
            }

        })) {


            Uni<RaisedBed.MapViewByDay.CellStatus[][]> resUni = RaisedBed.MapViewByDay.getCellStatus(LocalDate.now(), rb1.getWidth(), rb1.getLength());
            RaisedBed.MapViewByDay.CellStatus[][] res = resUni.await().indefinitely();

            int i = 0;
            int j = 0;
            for (RaisedBed.MapViewByDay.CellStatus[] re : res) {
                System.out.println();
                for (RaisedBed.MapViewByDay.CellStatus cellStatus : re) {
                    System.out.print((cellStatus == RaisedBed.MapViewByDay.CellStatus.EMPTY ? "O" : "X") + " ");
                    if (i == 0 && j == 0) {
                        assertEquals(cellStatus, RaisedBed.MapViewByDay.CellStatus.TAKEN);
                    }
                    if (i == 6 && j == 6) {
                        assertEquals(cellStatus, RaisedBed.MapViewByDay.CellStatus.TAKEN);
                    }
                    if (i == 3 && j == 2) {
                        assertEquals(cellStatus, RaisedBed.MapViewByDay.CellStatus.TAKEN);
                    }

                    j++;
                }
                i++;
                j = 0;
            }

        }

    }
}
