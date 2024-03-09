package de.vlaasch.co2bil.controllers;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnergySourceWrapper {
    private List<EnergySourceData> energySources;

}
