package org.garden.planner.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.garden.planner.model.Item;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

@ApplicationScoped
public class PlannerService {

    @Inject MapService mapService;

    public void startPlanning(LocalDate startDate, Duration duration) {

//         mapService.gardenMapByDate(startDate).onItem().invoke(e -> {
//            Map<Map.Entry<Integer, Integer>, Item> map = e.getMap();
//            map.
//        });

        return;
    }
}
