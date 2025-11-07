// src/test/java/uk/tw/energy/service/TariffServiceTest.java
package uk.tw.energy.service;

import org.junit.jupiter.api.*;
import uk.tw.energy.domain.Tariff;
import uk.tw.energy.domain.TariffType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TariffServiceTest {

    private TariffService tariffService;

    @BeforeEach
    void setUp() {
        tariffService = new TariffService();
    }

    @Test
    void save_shouldStoreAndRetrieveTariff() {
        Tariff tariff = new Tariff(
                "Standard",
                "SupplierA",
                TariffType.FLAT_RATE,
                BigDecimal.valueOf(0.15),
                BigDecimal.valueOf(0.20),
                LocalDate.now(),
                null
        );

        tariffService.save(tariff);
        List<Tariff> allTariffs = tariffService.findAll();

        assertEquals(1, allTariffs.size());
        assertEquals("Standard", allTariffs.getFirst().getTariffName());
        assertEquals("SupplierA", allTariffs.getFirst().getSupplierName());
    }

    @Test
    void findAll_shouldReturnAllSavedTariffs() {
        Tariff t1 = new Tariff("T1", "S1", TariffType.FLAT_RATE, BigDecimal.ONE, BigDecimal.TEN, LocalDate.now(), null);
        Tariff t2 = new Tariff("T2", "S2", TariffType.FLAT_RATE, BigDecimal.TEN, BigDecimal.ONE, LocalDate.now(), null);

        tariffService.save(t1);
        tariffService.save(t2);

        List<Tariff> allTariffs = tariffService.findAll();
        assertEquals(2, allTariffs.size());
        assertTrue(allTariffs.stream().anyMatch(t -> t.getTariffName().equals("T1")));
        assertTrue(allTariffs.stream().anyMatch(t -> t.getTariffName().equals("T2")));
    }

    @Test
    void findAll_shouldReturnEmptyListIfNoTariffsSaved() {
        List<Tariff> allTariffs = tariffService.findAll();
        assertTrue(allTariffs.isEmpty());
    }
}
