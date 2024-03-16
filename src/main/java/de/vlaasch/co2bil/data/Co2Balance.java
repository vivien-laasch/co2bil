package de.vlaasch.co2bil.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Co2Balance {
    private String label;
    private double energy;
    private double co2;
}
