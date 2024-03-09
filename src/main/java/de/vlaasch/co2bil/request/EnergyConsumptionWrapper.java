package de.vlaasch.co2bil.request;

import java.util.List;

import de.vlaasch.co2bil.data.EnergyConsumption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnergyConsumptionWrapper {
    private List<EnergyConsumption> consumptions;

}
