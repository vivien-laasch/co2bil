package de.vlaasch.co2bil.controllers;

import lombok.Data;

@Data
public class EnergySourceData {
    private String id;
    private String description;
    private double consumption;

    private double emissionFactor;
}
