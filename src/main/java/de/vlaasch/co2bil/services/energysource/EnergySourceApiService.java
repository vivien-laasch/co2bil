package de.vlaasch.co2bil.services.energysource;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import de.vlaasch.co2bil.data.EnergySource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EnergySourceApiService {

    private final EnergySourceApiProperties energySourceApiProperties;

    @Autowired
    public EnergySourceApiService(EnergySourceApiProperties ExternalEnergySourceApiProperties) {
        this.energySourceApiProperties = ExternalEnergySourceApiProperties;
    }

    @SuppressWarnings("null")
    public List<EnergySource> getEnergySources() {
        String energySourcesApiUrl = energySourceApiProperties.getEnergySourcesApiUrl();
        Validate.notBlank(energySourcesApiUrl, "Energy sources API URL must not be blank!");

        log.debug(String.format("Getting energy sources from external API: %s ", energySourcesApiUrl));

        RestTemplate template = new RestTemplate();
        EnergySource[] sources = template.getForObject(energySourcesApiUrl, EnergySource[].class);

        return Arrays.asList(sources);
    }
}