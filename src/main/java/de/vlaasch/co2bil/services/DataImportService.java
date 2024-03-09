package de.vlaasch.co2bil.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.vlaasch.co2bil.entities.EnergySource;
import de.vlaasch.co2bil.repositories.EnergySourceRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataImportService {

    @Autowired
    private EnergySourceRepository energySourceRepository;

    @PostConstruct
    public void importData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/data/energy-sources.json");) {
            List<EnergySource> sources = objectMapper.readValue(is, new TypeReference<List<EnergySource>>() {
            });

            if (sources != null)
                energySourceRepository.saveAll(sources);

        } catch (IOException e) {
            log.error("Could not import energy sources.", e);
        }
    }
}
