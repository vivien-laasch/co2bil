package de.vlaasch.co2bil.services.externalapi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "external.api")
@Data
public class ExternalApiProperties {

    private String energySourcesApiUrl;

}