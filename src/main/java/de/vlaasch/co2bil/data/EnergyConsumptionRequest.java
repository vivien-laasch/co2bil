package de.vlaasch.co2bil.data;

import lombok.Data;

@Data
public class EnergyConsumptionRequest {
    private String id;
    private String description;
    private double consumption;
    private Double emissionFactor;
}
