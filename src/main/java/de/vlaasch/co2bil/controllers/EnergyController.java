package de.vlaasch.co2bil.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.vlaasch.co2bil.data.Co2Balance;
import de.vlaasch.co2bil.data.EnergySource;
import de.vlaasch.co2bil.data.EnergyUsageEntry;
import de.vlaasch.co2bil.exceptions.EnergySourceNotFoundException;
import de.vlaasch.co2bil.exceptions.InvalidEnergyUsageException;
import de.vlaasch.co2bil.exceptions.NoEnergySourcesFoundException;
import de.vlaasch.co2bil.requests.EnergyUsageWrapper;
import de.vlaasch.co2bil.services.emission.Co2BalanceService;
import de.vlaasch.co2bil.services.externalapi.ExternalApiService;

@RestController
@RequestMapping("/api/v1/energy")
public class EnergyController {

    private final Co2BalanceService co2BalanceService;
    private final ExternalApiService externalApiService;

    public EnergyController(Co2BalanceService co2BalanceService, ExternalApiService externalApiService) {
        this.co2BalanceService = co2BalanceService;
        this.externalApiService = externalApiService;
    }

    @ExceptionHandler({ InvalidEnergyUsageException.class, EnergySourceNotFoundException.class })
    public ResponseEntity<String> handleBadRequest(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoEnergySourcesFoundException.class)
    public ResponseEntity<String> handleInternalError(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @PostMapping(value = "/balance")
    public ResponseEntity<List<Co2Balance>> getCo2Balance(@RequestBody EnergyUsageWrapper wrapper)
            throws InvalidEnergyUsageException, EnergySourceNotFoundException, NoEnergySourcesFoundException {
        List<EnergySource> energySources = externalApiService.getEnergySources();
        List<EnergyUsageEntry> entries = wrapper.getEntries();

        if (energySources == null)
            throw new NoEnergySourcesFoundException("Could not find any energy sources.");

        if (entries == null)
            throw new InvalidEnergyUsageException("Energy usages entries must not be empty.");

        return ResponseEntity.ok(co2BalanceService.getCo2Balance(entries, energySources));
    }

    @GetMapping(value = "/sources")
    public ResponseEntity<List<EnergySource>> getEnergySourcesExternal()
            throws InvalidEnergyUsageException, EnergySourceNotFoundException, NoEnergySourcesFoundException {
        List<EnergySource> energySources = externalApiService.getEnergySources();

        if (energySources == null)
            throw new NoEnergySourcesFoundException("Could not find any energy sources.");
            
        return ResponseEntity.ok(energySources);
    }
}
