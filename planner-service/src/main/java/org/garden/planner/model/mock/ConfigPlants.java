package org.garden.planner.model.mock;


import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface ConfigPlants {

    String getName();

    List<Map.Entry<Month, Month>> getPlantingSeasons();

    Duration getGrowingDuration();

    int[] getSpace();


}
