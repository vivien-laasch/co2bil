package de.vlaasch.co2bil.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.vlaasch.co2bil.data.EnergyConsumptionRequest;
import de.vlaasch.co2bil.data.EnergyConsumptionResponse;
import de.vlaasch.co2bil.data.EnergySource;
import de.vlaasch.co2bil.services.energysource.EnergySourceApiService;

@RestController
@RequestMapping("/energy-sources")
public class EnergySourceController {

    @Autowired
    EnergySourceApiService energySourceApiService;

    @PostMapping(value = "/consumption")
    public ResponseEntity<List<EnergyConsumptionResponse>> getConsumption(
            @RequestBody EnergySourceWrapper sources) {

        // Assume the API only has a single "getAll" endpoint, so we get all sources here.
        List<EnergySource> sourcesExternal = energySourceApiService.getEnergySources();
        List<EnergyConsumptionResponse> consumptionList = new ArrayList<>();
        for (EnergyConsumptionRequest cons : sources.getEnergySources()) {
            String id = cons.getId();
            String description = cons.getDescription();
            double consumption = cons.getConsumption();

            Double emissionFactor = cons.getEmissionFactor();
            Validate.notNull(id, "Energy Source Id, descrption and consumption must not be emtpy!", description,
                    consumption);
            if (emissionFactor == null) {
                // find matching id in external sources
                // sourcesExternal.stream().filter(src ->
                // src.getEnergySourceId.equals(id)).collect()
                emissionFactor = 1.0;
            }
            double totalCons = consumption * emissionFactor;
            EnergyConsumptionResponse test = new EnergyConsumptionResponse();
            test.setLabel(description);
            test.setEnergy(totalCons);
            test.setCo2(consumption);
            consumptionList.add(test);
        }

        return ResponseEntity.ok(consumptionList);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EnergySource>> getEnergySources() {
        return ResponseEntity.ok(energySourceApiService.getEnergySources());
    }
}
