package de.vlaasch.co2bil.request;

import java.util.List;

import de.vlaasch.co2bil.data.ConsumptionForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnergySourceWrapper {
    private List<ConsumptionForm> energySources;

}
