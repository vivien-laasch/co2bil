package de.vlaasch.co2bil.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnergySource {
    private String energySourceId;
    private String scopeId;
    private String name;
    private Double conversionFactor;
    private Double emissionFactor;
}