package de.vlaasch.co2bil.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.vlaasch.co2bil.data.ConsumptionCalculation;
import de.vlaasch.co2bil.data.ConsumptionForm;
import de.vlaasch.co2bil.data.EnergySource;
import de.vlaasch.co2bil.request.EnergySourceWrapper;
import de.vlaasch.co2bil.services.energysource.EnergySourceApiService;

@RestController
@RequestMapping("/energy-sources")
public class EnergySourceController {

    @Autowired
    EnergySourceApiService energySourceApiService;

    @PostMapping(value = "/consumption")
    public ResponseEntity<List<ConsumptionCalculation>> getConsumption(
            @RequestBody EnergySourceWrapper sources) {

        // Fetch external energy sources from the API, assume there is only "getAll"
        // method for now
        List<EnergySource> externalSources = energySourceApiService.getEnergySources();

        List<ConsumptionCalculation> consumptionList = new ArrayList<>();

        for (ConsumptionForm source : sources.getEnergySources()) {
            String id = source.getId();
            String description = source.getDescription();
            double consumption = source.getConsumption();

            Double emissionFactor = source.getEmissionFactor();

            Validate.notNull(id, "Energy Source Id must not be empty!");
            Validate.notNull(description, "Energy Source Description must not be empty!");
            Validate.isTrue(consumption > 0, "Energy Consumption must be a positive value!");

            // If emission factor is not provided, use a default value
            if (emissionFactor == null) {
                Optional<EnergySource> matchingSource = externalSources.stream()
                        .filter(src -> id.equals(src.getEnergySourceId()))
                        .findFirst();
                if (matchingSource.isPresent()) {
                    emissionFactor = matchingSource.get().getEmissionFactor();
                } else {
                    // return ResponseEntity.badRequest();
                    // Todo
                    emissionFactor = 1.0;
                }
            }

            // Calculate total energy consumption and CO2 emissions
            double totalEnergy = consumption * emissionFactor;

            // Create ConsumptionCalculation object
            ConsumptionCalculation calculation = new ConsumptionCalculation();
            calculation.setLabel(description);
            calculation.setEnergy(totalEnergy);
            calculation.setCo2(consumption);

            // Add the calculation to the list
            consumptionList.add(calculation);
        }

        return ResponseEntity.ok(consumptionList);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EnergySource>> getEnergySources() {
        return ResponseEntity.ok(energySourceApiService.getEnergySources());
    }
}
