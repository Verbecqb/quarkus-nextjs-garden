package org.garden.planner.service;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.garden.planner.model.CellStatus;
import org.garden.planner.model.RaisedBed;
import org.garden.planner.model.mock.ConfigPlants;
import org.garden.planner.model.mock.MySinglePickupPlants;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.next;

@ApplicationScoped
public class PlannerService {

    @Inject
    RaisedBedService mapService;

     List<ConfigPlants> getAvailableForSeedingByDate(LocalDate date) {

        return Arrays.stream(MySinglePickupPlants.values()).filter(mySinglePickupPlants ->

                mySinglePickupPlants.getPlantingSeasons().stream().noneMatch(plantingSeason ->
                        date.getMonth().getValue() > plantingSeason.getKey().getValue() &&
                                date.getMonth().getValue() < plantingSeason.getValue().getValue()
                )

        ).collect(Collectors.toList());
    }

    public void startPlanning(LocalDate startDate, Duration duration) {


        RaisedBed.<RaisedBed>findAll().list()

                .onItem().ifNotNull().invoke(e -> {

                    // Start on a Monday
                    LocalDate currentDate = startDate.with(DayOfWeek.MONDAY);
                    LocalDate endDate = startDate.plus(duration);

                    // Get the list of plants which can grow during this season
                    // TODO integrate the notion of seeding season
                    // Move Plant config to a standard interface + classes and move "getAvailaibleForSeedingByDate" there
                    List<ConfigPlants> listPlants = getAvailableForSeedingByDate(currentDate);
                    if (listPlants.isEmpty()) {
                        Log.info("No plants are available for this season");
                        return;
                    }

                    while (currentDate.isBefore(endDate)) {
                        for (RaisedBed raisedBed : e) {

                            Uni<CellStatus[][]> cellStatus = mapService.getCellStatus(raisedBed, startDate);


                        }

                        currentDate = currentDate.with(next(DayOfWeek.MONDAY));
                    }
                });

    }
}
