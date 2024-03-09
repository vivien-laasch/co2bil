package de.vlaasch.co2bil.entities;

import lombok.Data;

@Data
public class EnergySource {
    private String energySourceId;
    private String scopeId;
    private String name;
    private float conversionFactor;
    private float emissionFactor;
}