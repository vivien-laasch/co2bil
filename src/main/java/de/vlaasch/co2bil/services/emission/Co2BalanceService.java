package de.vlaasch.co2bil.services.emission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import de.vlaasch.co2bil.data.Co2Balance;
import de.vlaasch.co2bil.data.EnergySource;
import de.vlaasch.co2bil.data.EnergyUsageEntry;
import de.vlaasch.co2bil.exceptions.EnergySourceNotFoundException;
import de.vlaasch.co2bil.exceptions.InvalidEnergyUsageException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Co2BalanceService {

    public List<Co2Balance> getCo2Balance(@NonNull List<EnergyUsageEntry> usages,
            @NonNull List<EnergySource> energySources)
            throws InvalidEnergyUsageException, EnergySourceNotFoundException {

        log.debug("Start calculating CO2 emissions of {} entries.", usages.size());

        List<Co2Balance> calculations = new ArrayList<>();
        for (EnergyUsageEntry usage : usages) {
            String id = usage.getId();
            String description = usage.getDescription();
            Double consumption = usage.getConsumption();
            Double emissionFactor = usage.getEmissionFactor();

            log.trace("Processing energy usage entry: id={}, description={}, consumption={}, emissionFactor={}", id,
                    description, consumption, emissionFactor);

            if (id == null || description == null || consumption == null) {
                throw new InvalidEnergyUsageException(
                        "Invalid input: Energy Source Id, Description, and Consumption must not be empty!");
            }

            EnergySource matchingSource = energySources.stream()
                    .filter(src -> id.equals(src.getEnergySourceId()))
                    .findFirst().orElse(null);

            if (matchingSource == null)
                throw new EnergySourceNotFoundException(
                        String.format("Could not find a matching energy source for id %s.", id));

            if (emissionFactor == null) {
                emissionFactor = matchingSource.getEmissionFactor();
                log.trace("Using default emission factor {} for energy source {}.", emissionFactor, id);
            }

            double energy = consumption * matchingSource.getConversionFactor();
            double co2 = energy * emissionFactor;

            Co2Balance calc = new Co2Balance();
            calc.setLabel(String.format("%s (%s)", matchingSource.getName(), description));
            calc.setEnergy(energy);
            calc.setCo2(co2);
            calculations.add(calc);

            log.trace("Calculation completed for energy usage entry: label={}, energy={}, co2={}", calc.getLabel(),
                    calc.getEnergy(), calc.getCo2());
        }

        return calculations;
    }
}
