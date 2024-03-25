package org.garden.planner.model.mock;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.Month;
import java.util.*;

@Getter
@AllArgsConstructor
public enum MySinglePickupPlants implements SinglePickup {

    SALAD ("Common Salad",
            Arrays.asList(
                    new AbstractMap.SimpleEntry<>(Month.SEPTEMBER, Month.DECEMBER),
                    new AbstractMap.SimpleEntry<>(Month.MARCH, Month.JULY)
            ),
            Duration.ofDays(90),
            new AbstractMap.SimpleEntry<>(3, 3)) {

    },
    SPINACH("Common spinach",
            Arrays.asList(
            new AbstractMap.SimpleEntry<>(Month.OCTOBER, Month.DECEMBER),
            new AbstractMap.SimpleEntry<>(Month.FEBRUARY, Month.JUNE)
            ),
            Duration.ofDays(60),
            new AbstractMap.SimpleEntry<>(1, 1)) {

    },
    BROCOLI("Common brocoli",
             Arrays.asList(
                     new AbstractMap.SimpleEntry<>(Month.OCTOBER, Month.DECEMBER),
            new AbstractMap.SimpleEntry<>(Month.FEBRUARY, Month.JUNE)
            ),
            Duration.ofDays(60),
            new AbstractMap.SimpleEntry<>(4, 4)) {

    };

    private final String name;
    private final List<Map.Entry<Month, Month>> plantingSeasons;
    private final Duration growingDuration;
    private final Map.Entry<Integer, Integer> space;


}
