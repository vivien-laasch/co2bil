package de.vlaasch.co2bil.requests;

import java.util.List;

import de.vlaasch.co2bil.data.EnergyUsageEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnergyUsageWrapper {
    private List<EnergyUsageEntry> entries;

}
