package de.vlaasch.co2bil.data;

import lombok.Data;

@Data
public class EnergySource {
    private String energySourceId;
    private String scopeId;
    private String name;
    private double conversionFactor;
    private double emissionFactor;
}