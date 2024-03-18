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
import de.vlaasch.co2bil.exceptions.ExternalEnergySourcesNotFoundException;
import de.vlaasch.co2bil.exceptions.InvalidEnergyUsageException;
import de.vlaasch.co2bil.requests.EnergyUsageWrapper;
import de.vlaasch.co2bil.services.emission.Co2BalanceService;
import de.vlaasch.co2bil.services.externalapi.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/energy")
@RequiredArgsConstructor
@Slf4j
public class EnergyController {

    private final Co2BalanceService co2BalanceService;
    private final ExternalApiService externalApiService;

    @ExceptionHandler({ InvalidEnergyUsageException.class, EnergySourceNotFoundException.class })
    public ResponseEntity<String> handleBadRequest(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ExternalEnergySourcesNotFoundException.class)
    public ResponseEntity<String> handleNoEnergySources(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedError(Exception e) {
        log.error("An unhandled error occurred.", e);
        return ResponseEntity.internalServerError().body("An unexpected error occurred, please try again later.");
    }

    @PostMapping(value = "/balance")
    public ResponseEntity<List<Co2Balance>> getCo2Balance(@RequestBody EnergyUsageWrapper wrapper)
            throws InvalidEnergyUsageException, EnergySourceNotFoundException, ExternalEnergySourcesNotFoundException {
        List<EnergySource> energySources = externalApiService.getEnergySources();
        List<EnergyUsageEntry> entries = wrapper.getEntries();

        if (energySources == null)
            throw new ExternalEnergySourcesNotFoundException("Could not find any energy sources.");

        if (entries == null)
            throw new InvalidEnergyUsageException("Energy usages entries must not be empty.");

        return ResponseEntity.ok(co2BalanceService.getCo2Balance(entries, energySources));
    }

    @GetMapping(value = "/sources")
    public ResponseEntity<List<EnergySource>> getEnergySourcesExternal()
            throws InvalidEnergyUsageException, EnergySourceNotFoundException, ExternalEnergySourcesNotFoundException {
        List<EnergySource> energySources = externalApiService.getEnergySources();

        if (energySources == null)
            throw new ExternalEnergySourcesNotFoundException("Could not find any energy sources.");

        return ResponseEntity.ok(energySources);
    }
}
