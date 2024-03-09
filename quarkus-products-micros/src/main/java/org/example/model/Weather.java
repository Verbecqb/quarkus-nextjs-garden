package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Weather {

    private long latitude;
    private long longitude;
    private Current current;

}

@Data
@AllArgsConstructor
@NoArgsConstructor
 class Current {
    private long temperature_2m;
    private int interval;
}

