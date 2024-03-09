package de.vlaasch.co2bil.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.vlaasch.co2bil.entities.EnergySource;
import de.vlaasch.co2bil.services.energysource.EnergySourceApiService;

@RestController
@RequestMapping("/energy-sources")
public class EnergySourceController {


    @Autowired
    EnergySourceApiService energySourceApiService;

    @PostMapping(value = "/calc")
    public ResponseEntity<List<EnergyConsumption>> calculateCarbonFootprint(@RequestBody EnergySourceWrapper sources) {
        
        return ResponseEntity.ok(null);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EnergySource>> getEnergySources() {
        return ResponseEntity.ok(energySourceApiService.getEnergySources());
    }
}
