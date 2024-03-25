package org.garden.planner.model.mock;


import java.time.Duration;
import java.time.Month;
import java.util.List;
import java.util.Map;

public interface  ConfigPlants {

    String getName();

    List<Map.Entry<Month, Month>> getPlantingSeasons();

    Duration getGrowingDuration();

    Map.Entry<Integer, Integer> getSpace();

}
