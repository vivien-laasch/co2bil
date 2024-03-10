package de.vlaasch.co2bil.data;

import lombok.Data;

@Data
public class EnergyUsageEntry {
    private String id;
    private String description;
    private Double consumption;
    private Double emissionFactor;
}
