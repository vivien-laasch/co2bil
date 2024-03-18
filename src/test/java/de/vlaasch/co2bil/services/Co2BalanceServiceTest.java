package de.vlaasch.co2bil.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.vlaasch.co2bil.data.Co2Balance;
import de.vlaasch.co2bil.data.EnergySource;
import de.vlaasch.co2bil.data.EnergyUsageEntry;
import de.vlaasch.co2bil.exceptions.EnergySourceNotFoundException;
import de.vlaasch.co2bil.exceptions.InvalidEnergyUsageException;
import de.vlaasch.co2bil.services.emission.Co2BalanceService;

public class Co2BalanceServiceTest {

    private Co2BalanceService co2BalanceService;
    private List<EnergySource> energySources;
    private List<EnergyUsageEntry> usages;

    @BeforeEach
    void setUp() {
        co2BalanceService = new Co2BalanceService();
        energySources = new ArrayList<>();
        usages = new ArrayList<>();
        energySources.add(
                new EnergySource("1",
                        "scope",
                        "Electricity",
                        2.0,
                        2.0));
    }

    @Test
    public void testGetCo2Balance_NoEmissionFactor() throws Exception {
        // Given
        EnergyUsageEntry usage = new EnergyUsageEntry();
        usage.setId("1");
        usage.setDescription("Description");
        usage.setConsumption(10.0);
        usages.add(usage);

        // When
        List<Co2Balance> result = co2BalanceService.getCo2Balance(usages, energySources);

        // Then
        assertEquals(1, result.size());
        Co2Balance balance = result.get(0);
        assertEquals("Electricity (Description)", balance.getLabel());
        assertEquals(20.0, balance.getEnergy());
        assertEquals(40.0, balance.getCo2());
    }

    @Test
    public void testGetCo2Balance_WithEmissionFactor() throws Exception {
        // Given
        EnergyUsageEntry usage = new EnergyUsageEntry();
        usage.setId("1");
        usage.setDescription("Description");
        usage.setConsumption(10.0);
        usage.setEmissionFactor(3.0);
        usages.add(usage);

        // When
        List<Co2Balance> result = co2BalanceService.getCo2Balance(usages, energySources);

        // Then
        assertEquals(1, result.size());
        Co2Balance balance = result.get(0);
        assertEquals("Electricity (Description)", balance.getLabel());
        assertEquals(20.0, balance.getEnergy());
        assertEquals(60.0, balance.getCo2());
    }

    @Test
    public void testGetCo2Balance_InvalidEnergyUsage() throws Exception {
        // Given
        EnergyUsageEntry usage = new EnergyUsageEntry();
        usage.setId("1");
        usage.setDescription("Description");
        usages.add(usage);

        // When
        assertThrows(InvalidEnergyUsageException.class,
                () -> co2BalanceService.getCo2Balance(usages, energySources));
    }

    @Test
    public void testGetCo2Balance_EnergySourceNotFound() throws Exception {
        // Given
        EnergyUsageEntry usage = new EnergyUsageEntry();
        usage.setId("2");
        usage.setDescription("Description");
        usage.setConsumption(10.0);
        usages.add(usage);

        // When
        assertThrows(EnergySourceNotFoundException.class,
                () -> co2BalanceService.getCo2Balance(usages, energySources));
    }
}
