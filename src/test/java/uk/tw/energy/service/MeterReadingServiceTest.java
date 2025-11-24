// src/test/java/uk/tw/energy/service/MeterReadingServiceTest.java
package uk.tw.energy.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.energy.domain.ElectricityReading;

public class MeterReadingServiceTest {

    private MeterReadingService meterReadingService;

    @BeforeEach
    public void setUp() {
        meterReadingService = new MeterReadingService(new HashMap<>());
    }

    @Test
    public void givenMeterIdThatDoesNotExistShouldReturnEmptyOptional() {
        assertThat(meterReadingService.getReadings("unknown-id")).isEqualTo(Optional.empty());
    }

    @Test
    public void givenMeterIdWithExistingReadingsShouldReturnReadings() {
        String meterId = "meter-1";
        List<ElectricityReading> readings = Arrays.asList(
                new ElectricityReading(Instant.now(), BigDecimal.valueOf(10)),
                new ElectricityReading(Instant.now(), BigDecimal.valueOf(20)));
        meterReadingService.storeReadings(meterId, readings);

        assertThat(meterReadingService.getReadings(meterId)).isEqualTo(Optional.of(readings));
    }

    @Test
    public void storeReadingsShouldAddToExistingMeter() {
        String meterId = "meter-2";
        List<ElectricityReading> initialReadings =
                Arrays.asList(new ElectricityReading(Instant.now(), BigDecimal.valueOf(5)));
        meterReadingService.storeReadings(meterId, initialReadings);

        List<ElectricityReading> newReadings = Arrays.asList(
                new ElectricityReading(Instant.now(), BigDecimal.valueOf(15)),
                new ElectricityReading(Instant.now(), BigDecimal.valueOf(25)));
        meterReadingService.storeReadings(meterId, newReadings);

        List<ElectricityReading> expected = new ArrayList<>(initialReadings);
        expected.addAll(newReadings);

        assertThat(meterReadingService.getReadings(meterId)).isEqualTo(Optional.of(expected));
    }

    @Test
    public void storeReadingsShouldAddToNewMeter() {
        String meterId = "meter-3";
        List<ElectricityReading> readings = List.of(new ElectricityReading(Instant.now(), BigDecimal.valueOf(30)));
        meterReadingService.storeReadings(meterId, readings);

        assertThat(meterReadingService.getReadings(meterId)).isEqualTo(Optional.of(readings));
    }
}
