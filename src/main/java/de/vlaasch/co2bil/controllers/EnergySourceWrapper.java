package de.vlaasch.co2bil.controllers;

import java.util.List;

import de.vlaasch.co2bil.data.EnergyConsumptionRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnergySourceWrapper {
    private List<EnergyConsumptionRequest> energySources;

}
