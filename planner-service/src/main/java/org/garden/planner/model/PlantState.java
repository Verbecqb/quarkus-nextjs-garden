package org.garden.planner.model;

import org.garden.planner.model.mock.SinglePickup;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public enum PlantState {

    SEED {
        @Override
        void nextStep(Item item) {
            item.setPlanted_date(LocalDate.now());
            item.setPickup_date( // Set planned picked-up date on a Monday
                    LocalDate.now().plus(item.getConfigPlants().getGrowingDuration())
                            .with(DayOfWeek.MONDAY)
            );
            item.setState(PlantState.PLANTED);
        }
    },

    PLANTED {
        @Override
        void nextStep(Item item) {
            item.setState(PlantState.PICKEDUP);
        }
    },

    PICKEDUP {
        @Override
        void nextStep(Item item) {

            // Item is single pick-up like salad, fail if try to move to next step
            if (item instanceof SinglePickup) {
                throw new IllegalArgumentException("Picked-up is a final state");
            }

            item.setPickup_date(// Set planned picked-up date on a Monday
                    LocalDate.now().plus(item.getConfigPlants().getGrowingDuration())
                            .with(DayOfWeek.MONDAY)
            );

        }
    };

    abstract void nextStep(Item item);

}
