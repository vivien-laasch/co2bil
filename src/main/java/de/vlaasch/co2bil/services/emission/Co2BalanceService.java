package de.vlaasch.co2bil.services.emission;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.vlaasch.co2bil.data.Co2Balance;
import de.vlaasch.co2bil.data.EnergySource;
import de.vlaasch.co2bil.data.EnergyUsageEntry;
import de.vlaasch.co2bil.exceptions.EnergySourceNotFoundException;
import de.vlaasch.co2bil.exceptions.InvalidEnergyUsageException;
import lombok.NonNull;
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
            BigDecimal consumption = usage.getConsumption() != null ? BigDecimal.valueOf(usage.getConsumption()) : null;
            BigDecimal emissionFactor = usage.getEmissionFactor() != null
                    ? BigDecimal.valueOf(usage.getEmissionFactor())
                    : null;

            log.trace("Processing energy usage entry: id={}, description={}, consumption={}, emissionFactor={}", id,
                    description, consumption, emissionFactor);

            if (id == null || description == null || consumption == null) {
                throw new InvalidEnergyUsageException(
                        "Invalid input: Energy Source Id, Description, and Consumption must not be empty!");
            }

            if (!isValidDecimalPlaces(consumption) || !isValidDecimalPlaces(emissionFactor)) {
                throw new InvalidEnergyUsageException(
                        "Invalid input: Emission Factor and Consumption must not be have more than 5 decimal places!");
            }

            EnergySource matchingSource = energySources.stream()
                    .filter(src -> id.equals(src.getEnergySourceId()))
                    .findFirst().orElse(null);

            if (matchingSource == null)
                throw new EnergySourceNotFoundException(
                        String.format("Could not find a matching energy source for id %s.", id));

            if (emissionFactor == null) {
                emissionFactor = BigDecimal.valueOf(matchingSource.getEmissionFactor());
                log.trace("Using default emission factor {} for energy source {}.", emissionFactor, id);
            }

            BigDecimal conversionFactor = BigDecimal.valueOf(matchingSource.getConversionFactor());

            BigDecimal energy = consumption.multiply(conversionFactor);
            BigDecimal co2 = energy.multiply(emissionFactor).divide(BigDecimal.valueOf(1000));
            
            //Assume desired output precision of 2 decimal places
            energy = energy.setScale(2, RoundingMode.HALF_UP);
            co2 = co2.setScale(2, RoundingMode.HALF_UP);

            Co2Balance calc = new Co2Balance();
            calc.setLabel(String.format("%s (%s)", matchingSource.getName(), description));
            calc.setEnergy(energy.doubleValue());
            calc.setCo2(co2.doubleValue());
            calculations.add(calc);

            log.trace("Calculation completed for energy usage entry: label={}, energy={}, co2={}", calc.getLabel(),
                    calc.getEnergy(), calc.getCo2());
        }

        return calculations;
    }

    private boolean isValidDecimalPlaces(BigDecimal value) {
        if (value == null)
            return true;

        return value.scale() <= 5;
    }
}
