package de.vlaasch.co2bil.services.energysource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "external.energy-sources-api")
@Data
public class EnergySourceApiProperties {

    private String energySourcesApiUrl;

}