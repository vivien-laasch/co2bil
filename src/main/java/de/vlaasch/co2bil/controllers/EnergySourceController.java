package de.vlaasch.co2bil.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.vlaasch.co2bil.data.ConsumptionCalculation;
import de.vlaasch.co2bil.data.EnergyConsumption;
import de.vlaasch.co2bil.data.EnergySource;
import de.vlaasch.co2bil.request.EnergyConsumptionWrapper;
import de.vlaasch.co2bil.services.energysource.EnergySourceApiService;

@RestController
@RequestMapping("/energy-sources")
public class EnergySourceController {

    @Autowired
    EnergySourceApiService energySourceApiService;

    @PostMapping(value = "/consumption")
    public ResponseEntity<?> getConsumption(@RequestBody EnergyConsumptionWrapper consumptions) {

        // Fetch external energy sources from the API, assume there is only "getAll"
        // method for now
        List<EnergySource> externalSources = energySourceApiService.getEnergySources();

        List<ConsumptionCalculation> calculations = new ArrayList<>();

        for (EnergyConsumption cons : consumptions.getConsumptions()) {
            String id = cons.getId();
            String description = cons.getDescription();
            Double consumption = cons.getConsumption();
            Double emissionFactor = cons.getEmissionFactor();

            if (id == null || description == null || consumption == null) {
                return ResponseEntity.badRequest().body(
                        "Invalid input: Energy Source Id, Description, and Consumption must not be empty!");
            }

            EnergySource matchingSource = externalSources.stream()
                    .filter(src -> id.equals(src.getEnergySourceId()))
                    .findFirst().orElse(null);

            if (matchingSource == null)
                return ResponseEntity.badRequest()
                        .body(String.format("Could not find a matching energy source for id %s.", id));

            if (emissionFactor == null)
                emissionFactor = matchingSource.getEmissionFactor();

            double energy = consumption * matchingSource.getConversionFactor();
            double co2 = energy * emissionFactor;

            ConsumptionCalculation calc = new ConsumptionCalculation();
            calc.setLabel(String.format("%s (%s)", matchingSource.getName(), description));
            calc.setEnergy(energy);
            calc.setCo2(co2);
            calculations.add(calc);
        }
        return ResponseEntity.ok(calculations);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EnergySource>> getEnergySources() {
        return ResponseEntity.ok(energySourceApiService.getEnergySources());
    }
}
