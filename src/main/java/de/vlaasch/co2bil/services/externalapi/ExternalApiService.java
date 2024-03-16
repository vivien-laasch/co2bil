package de.vlaasch.co2bil.services.externalapi;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import de.vlaasch.co2bil.data.EnergySource;
import de.vlaasch.co2bil.exceptions.ExternalEnergySourcesNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExternalApiService {

    private final ExternalApiProperties externalApiProperties;
        
    public ExternalApiService(ExternalApiProperties externalApiProperties) {
        this.externalApiProperties = externalApiProperties;
    }

    @SuppressWarnings("null")
    public List<EnergySource> getEnergySources() throws ExternalEnergySourcesNotFoundException {
        String energySourcesApiUrl = externalApiProperties.getEnergySourcesApiUrl();
        Validate.notBlank(energySourcesApiUrl,
                "Energy sources API URL (external.api.energySourcesApiUrl) must not be blank!");

        log.debug("Getting energy sources from external API: {} ", energySourcesApiUrl);
        
        RestClient client = RestClient.create(energySourcesApiUrl);
        ResponseEntity<EnergySource[]> sources = client.get().retrieve().toEntity(EnergySource[].class);
        return Arrays.asList(sources.getBody());
    }
}