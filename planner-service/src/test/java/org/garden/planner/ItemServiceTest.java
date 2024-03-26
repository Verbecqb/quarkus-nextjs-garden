package org.garden.planner;

import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.MockitoConfig;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;
import org.apache.commons.lang3.ArrayUtils;
import org.garden.planner.model.CellStatus;
import org.garden.planner.model.Item;
import org.garden.planner.model.RaisedBed;
import org.garden.planner.model.mock.MySinglePickupPlants;
import org.garden.planner.service.ItemService;
import org.garden.planner.service.RaisedBedService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class ItemServiceTest {

    @InjectMock
    @MockitoConfig(convertScopes = true)
    RaisedBedService raisedBedService;

    @Inject
    ItemService itemService;

    private void displayRaisedBed(CellStatus[][] cellStatuses) {

        int i = 0;
        int j = 0;

        for (int tmp = 0; tmp < cellStatuses[0].length; tmp++) {
            if (tmp == 0) System.out.print("  " + tmp + " ");
            else System.out.print(tmp + " ");
        }

        for (CellStatus[] re : cellStatuses) {
            System.out.println();
            for (CellStatus cellStatus : re) {
                if (j == 0) System.out.print(i + " ");
                System.out.print(
                        (cellStatus == CellStatus.EMPTY ? "O" : (cellStatus == CellStatus.TAKEN_CENTER ? "M" : "X")) + " ");
                j++;
            }
            i++;
            j = 0;
        }
        System.out.println();
    }

    static int BED_WIDTH = 6;
    static int BED_LENGTH = 8;
    static RaisedBed bed;

    @BeforeAll()
    static void setup_before_all() {

        bed = new RaisedBed();
        bed.setWidth(BED_WIDTH);
        bed.setLength(BED_LENGTH);
    }


    @BeforeEach
    void setup() {


        CellStatus[][] mockedCellStatus = new CellStatus[BED_WIDTH][BED_LENGTH];
        for (CellStatus[] cell : mockedCellStatus) {
            Arrays.fill(cell, CellStatus.EMPTY);
        }

        mockedCellStatus[1][1] = CellStatus.TAKEN;
        mockedCellStatus[2][1] = CellStatus.TAKEN;
        mockedCellStatus[3][1] = CellStatus.TAKEN;
        mockedCellStatus[1][2] = CellStatus.TAKEN;
        mockedCellStatus[2][2] = CellStatus.TAKEN;
        mockedCellStatus[3][2] = CellStatus.TAKEN;

        Mockito.when(raisedBedService.getCellStatus(any(), any())).thenReturn(Uni.createFrom().item(mockedCellStatus));

        displayRaisedBed(mockedCellStatus);

    }

    @Test
    void post_item_on_empty_location_success() {

        Item item = Item.builder()
                .planted_date(LocalDate.now())
                .configPlants(MySinglePickupPlants.SPINACH)
                .location(Arrays.asList(0, 0))
                .raisedBed(bed)
                .build();

        System.out.println("Test scenario: Adding a " + MySinglePickupPlants.SPINACH.getName() +
                " \n Coordinate: x=" + item.getLocation().get(0) + " , y=" + item.getLocation().get(1) +
                " \n Space needed: width=" + item.getConfigPlants().getSpace()[0] + " , length=" + item.getConfigPlants().getSpace()[1]);

        Uni<Item> uni = itemService.post(item);

        UniAssertSubscriber<Item> subscriber = uni
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.awaitItem().assertCompleted().assertItem(item);
    }

    @Test
    void post_item_on_empty_location_taken() {

        Item item_spinach = Item.builder()
                .planted_date(LocalDate.now())
                .configPlants(MySinglePickupPlants.SPINACH)
                .location(Arrays.asList(3, 1))
                .raisedBed(bed)
                .build();

        System.out.println("Test scenario: Adding a " + MySinglePickupPlants.SPINACH.getName() +
                " \n Coordinate: x=" + item_spinach.getLocation().get(0) + " , y=" + item_spinach.getLocation().get(1) +
                " \n Space needed: width=" + item_spinach.getConfigPlants().getSpace()[0] + " , length=" + item_spinach.getConfigPlants().getSpace()[1]);

        Uni<Item> uni = itemService.post(item_spinach);

        UniAssertSubscriber<Item> subscriber = uni
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.assertFailedWith(IllegalArgumentException.class);
    }

    @Test
    void post_item_on_empty_location_empty_but_not_enough_space() {

        Item item = Item.builder()
                .planted_date(LocalDate.now())
                .configPlants(MySinglePickupPlants.SALAD)
                .location(Arrays.asList(4, 1))
                .raisedBed(bed)
                .build();

        System.out.println("Test scenario: Adding a " + MySinglePickupPlants.SPINACH.getName() +
                " \n Coordinate: x=" + item.getLocation().get(0) + " , y=" + item.getLocation().get(1) +
                " \n Space needed: width=" + item.getConfigPlants().getSpace()[0] + " , length=" + item.getConfigPlants().getSpace()[1]);

        Uni<Item> uni = itemService.post(item);

        UniAssertSubscriber<Item> subscriber = uni
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.assertFailedWith(IllegalArgumentException.class);
    }

    @Test
    void post_item_on_location_outside_of_raised_bed() {

        Item item = Item.builder()
                .planted_date(LocalDate.now())
                .configPlants(MySinglePickupPlants.SPINACH)
                .location(Arrays.asList(6, 6))
                .raisedBed(bed)
                .build();

        System.out.println("Test scenario: Adding a " + MySinglePickupPlants.SPINACH.getName() +
                " \n Coordinate: x=" + item.getLocation().get(0) + " , y=" + item.getLocation().get(1) +
                " \n Space needed: width=" + item.getConfigPlants().getSpace()[0] + " , length=" + item.getConfigPlants().getSpace()[1]);

        Uni<Item> uni = itemService.post(item);

        UniAssertSubscriber<Item> subscriber = uni
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.assertFailedWith(IllegalArgumentException.class);
    }

    @Test
    void post_item_on_empty_location_empty_space_success() {

        Item item = Item.builder()
                .planted_date(LocalDate.now())
                .configPlants(MySinglePickupPlants.BROCOLI)
                .location(Arrays.asList(2, 6))
                .raisedBed(bed)
                .build();

        System.out.println("Test scenario: Adding a " + item.getConfigPlants().getName() +
                " \n Coordinate: x=" + item.getLocation().get(0) + " , y=" + item.getLocation().get(1) +
                " \n Space needed: width=" + item.getConfigPlants().getSpace()[0] + " , length=" + item.getConfigPlants().getSpace()[1]);

        Uni<Item> uni = itemService.post(item);

        UniAssertSubscriber<Item> subscriber = uni
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.awaitItem().assertCompleted().assertItem(item);

    }

    @Test
    void post_item_on_empty_location_empty_but_space_outside() {

        Item item = Item.builder()
                .planted_date(LocalDate.now())
                .configPlants(MySinglePickupPlants.BROCOLI)
                .location(Arrays.asList(0, 6))
                .raisedBed(bed)
                .build();

        System.out.println("Test scenario: Adding a " + item.getConfigPlants().getName() +
                " \n Coordinate: x=" + item.getLocation().get(0) + " , y=" + item.getLocation().get(1) +
                " \n Space needed: width=" + item.getConfigPlants().getSpace()[0] + " , length=" + item.getConfigPlants().getSpace()[1]);

        Uni<Item> uni = itemService.post(item);

        UniAssertSubscriber<Item> subscriber = uni
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.assertFailedWith(IllegalArgumentException.class);

    }

    @Test
    void getCellRequired_space_one_square_space_success() {

        int X = 4;
        int Y = 7;
        int WIDTH_SPACE = 1;
        int LENGTH_SPACE = 1;

        System.out.println("Test scenario: Get cells required for " +
                " \n Coordinate: x=" + X + " , y=" + Y +
                " \n Space needed: width=" + WIDTH_SPACE + " , length=" + LENGTH_SPACE);

        List<int[]> res = itemService.getCellsRequired(X, Y, WIDTH_SPACE, LENGTH_SPACE);

        CellStatus[][] cellStatus = new CellStatus[BED_WIDTH][BED_LENGTH];
        for (CellStatus[] cell : cellStatus) {
            Arrays.fill(cell, CellStatus.EMPTY);
        }

        for (int[] cell : res) {
            cellStatus[cell[0]][cell[1]] = CellStatus.TAKEN;
            if (cell[0] == X && cell[1] == Y) cellStatus[cell[0]][cell[1]] = CellStatus.TAKEN_CENTER;

        }

        displayRaisedBed(cellStatus);


        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {4,7})));

        // Some random location to confirm those are empty cells
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,2})));
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {1,6})));
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {1,7})));
    }

    @Test
    void getCellRequired_space_rectangle_success() {

        int X = 4;
        int Y = 7;
        int WIDTH_SPACE = 4;
        int LENGTH_SPACE = 2;

        System.out.println("Test scenario: Get cells required for " +
                " \n Coordinate: x=" + X + " , y=" + Y +
                " \n Space needed: width=" + WIDTH_SPACE + " , length=" + LENGTH_SPACE);

        List<int[]> res = itemService.getCellsRequired(X, Y, WIDTH_SPACE, LENGTH_SPACE);

        CellStatus[][] cellStatus = new CellStatus[BED_WIDTH][BED_LENGTH];
        for (CellStatus[] cell : cellStatus) {
            Arrays.fill(cell, CellStatus.EMPTY);
        }

        for (int[] cell : res) {
            cellStatus[cell[0]][cell[1]] = CellStatus.TAKEN;
            if (cell[0] == X && cell[1] == Y) cellStatus[cell[0]][cell[1]] = CellStatus.TAKEN_CENTER;

        }

        displayRaisedBed(cellStatus);


        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {2,6})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {2,7})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,6})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,7})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {4,6})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {4,7})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {5,6})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {5,7})));

        // Some random location to confirm those are empty cells
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,2})));
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {1,6})));
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {1,7})));
    }

    @Test
    void getCellRequired_space_odd_square_success() {

        int X = 2;
        int Y = 6;
        int WIDTH_SPACE = 4;
        int LENGTH_SPACE = 4;

        System.out.println("Test scenario: Get cells required for " +
                " \n Coordinate: x=" + X + " , y=" + Y +
                " \n Space needed: width=" + WIDTH_SPACE + " , length=" + LENGTH_SPACE);

        List<int[]> res = itemService.getCellsRequired(X, Y, WIDTH_SPACE, LENGTH_SPACE);

        CellStatus[][] cellStatus = new CellStatus[BED_WIDTH][BED_LENGTH];
        for (CellStatus[] cell : cellStatus) {
            Arrays.fill(cell, CellStatus.EMPTY);
        }

        for (int[] cell : res) {
            cellStatus[cell[0]][cell[1]] = CellStatus.TAKEN;
            if (cell[0] == X && cell[1] == Y) cellStatus[cell[0]][cell[1]] = CellStatus.TAKEN_CENTER;

        }

        displayRaisedBed(cellStatus);


        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {0,4})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {0,5})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {0,6})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {0,7})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,4})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,5})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,6})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,7})));

        // Some random location to confirm those are empty cells
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,2})));
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {4,6})));
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {4,7})));
    }

    @Test
    void getCellRequired_space_even_square_success() {

        int X = 2;
        int Y = 3;
        int WIDTH_SPACE = 3;
        int LENGTH_SPACE = 3;

        System.out.println("Test scenario: Get cells required for " +
                " \n Coordinate: x=" + X + " , y=" + Y +
                " \n Space needed: width=" + WIDTH_SPACE + " , length=" + LENGTH_SPACE);

        List<int[]> res = itemService.getCellsRequired(X, Y, WIDTH_SPACE, LENGTH_SPACE);

        CellStatus[][] cellStatus = new CellStatus[BED_WIDTH][BED_LENGTH];
        for (CellStatus[] cell : cellStatus) {
            Arrays.fill(cell, CellStatus.EMPTY);
        }

        for (int[] cell : res) {
            cellStatus[cell[0]][cell[1]] = CellStatus.TAKEN;
            if (cell[0] == X && cell[1] == Y) cellStatus[cell[0]][cell[1]] = CellStatus.TAKEN_CENTER;

        }

        displayRaisedBed(cellStatus);


        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {1,2})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {1,3})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {1,4})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {2,2})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {2,3})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {2,4})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,2})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,3})));
        Assertions.assertTrue(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,4})));

        // Some random location to confirm those are empty cells
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {3,5})));
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {4,3})));
        Assertions.assertFalse(Arrays.stream(res.toArray()).anyMatch(x -> Arrays.equals((int[]) x, new int[] {4,4})));
    }

}

