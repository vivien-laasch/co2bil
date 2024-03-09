package de.vlaasch.co2bil.data;

import lombok.Data;

@Data
public class EnergyConsumptionResponse {
    private String label;
    private double energy;
    private double co2;
}
