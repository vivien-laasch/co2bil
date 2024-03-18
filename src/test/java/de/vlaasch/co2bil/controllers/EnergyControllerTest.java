package de.vlaasch.co2bil.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.vlaasch.co2bil.data.Co2Balance;
import de.vlaasch.co2bil.data.EnergySource;
import de.vlaasch.co2bil.data.EnergyUsageEntry;
import de.vlaasch.co2bil.exceptions.ExternalEnergySourcesNotFoundException;
import de.vlaasch.co2bil.exceptions.InvalidEnergyUsageException;
import de.vlaasch.co2bil.requests.EnergyUsageWrapper;
import de.vlaasch.co2bil.services.emission.Co2BalanceService;
import de.vlaasch.co2bil.services.externalapi.ExternalApiService;

@SuppressWarnings("unchecked")
public class EnergyControllerTest {
    ExternalApiService externalApiService = mock(ExternalApiService.class);
    Co2BalanceService co2BalanceService = mock(Co2BalanceService.class);
    EnergyController energyController = new EnergyController(co2BalanceService, externalApiService);
    EnergyUsageWrapper wrapper = mock(EnergyUsageWrapper.class);

    @BeforeEach
    void setUp() throws Exception {
        when(externalApiService.getEnergySources()).thenReturn(mock(List.class));
    }

    @Test
    void testGetCo2Balance() throws Exception {
        // Given
        EnergyUsageEntry entry = new EnergyUsageEntry();

        // When
        when(co2BalanceService.getCo2Balance(any(), any())).thenReturn(mock(List.class));
        when(wrapper.getEntries()).thenReturn(List.of(entry));

        ResponseEntity<List<Co2Balance>> responseEntity = energyController.getCo2Balance(wrapper);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        verify(externalApiService, times(1)).getEnergySources();
        verify(co2BalanceService, times(1)).getCo2Balance(any(), any());
    }

    @Test
    void testGetEnergySourcesExternal() throws Exception {
        // When
        ResponseEntity<List<EnergySource>> responseEntity = energyController.getEnergySourcesExternal();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        verify(externalApiService, times(1)).getEnergySources();
    }

    @Test
    void testHandleBadRequest() throws Exception {
        // When
        when(wrapper.getEntries()).thenReturn(null);

        // Then
        assertThrows(InvalidEnergyUsageException.class, () -> energyController.getCo2Balance(wrapper));
    }

    @Test
    void testHandleNoEnergySources() throws Exception {
        // When
        when(co2BalanceService.getCo2Balance(any(), any())).thenReturn(mock(List.class));
        when(externalApiService.getEnergySources()).thenReturn(null);
        // Then
        assertThrows(ExternalEnergySourcesNotFoundException.class, () -> energyController.getCo2Balance(wrapper));
    }
    
}
