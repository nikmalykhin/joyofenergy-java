// src/test/java/uk/tw/energy/controller/TariffAdminControllerTest.java
package uk.tw.energy.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.tw.energy.domain.Tariff;
import uk.tw.energy.domain.TariffType;
import uk.tw.energy.service.TariffService;

@WebMvcTest(TariffAdminController.class)
class TariffAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TariffService tariffService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTariff_shouldReturnSavedTariff() throws Exception {
        Tariff tariff = new Tariff(
                "Standard",
                "SupplierA",
                TariffType.FLAT_RATE,
                BigDecimal.valueOf(0.15),
                BigDecimal.valueOf(0.20),
                LocalDate.of(2024, 1, 1),
                null);

        when(tariffService.save(any(Tariff.class))).thenReturn(tariff);

        mockMvc.perform(post("/admin/tariffs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tariff)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tariffName").value("Standard"))
                .andExpect(jsonPath("$.supplierName").value("SupplierA"))
                .andExpect(jsonPath("$.tariffType").value("FLAT_RATE"))
                .andExpect(jsonPath("$.unitRateKwh").value(0.15))
                .andExpect(jsonPath("$.standingChargePerDay").value(0.20))
                .andExpect(jsonPath("$.validFrom").value("2024-01-01"));
    }

    @Test
    void getAllTariffs_shouldReturnListOfTariffs() throws Exception {
        Tariff t1 = new Tariff(
                "T1", "S1", TariffType.FLAT_RATE, BigDecimal.ONE, BigDecimal.TEN, LocalDate.of(2024, 1, 1), null);
        Tariff t2 = new Tariff(
                "T2", "S2", TariffType.FLAT_RATE, BigDecimal.TEN, BigDecimal.ONE, LocalDate.of(2024, 2, 1), null);

        List<Tariff> tariffs = Arrays.asList(t1, t2);
        when(tariffService.findAll()).thenReturn(tariffs);

        mockMvc.perform(get("/admin/tariffs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].tariffName").value("T1"))
                .andExpect(jsonPath("$[1].tariffName").value("T2"));
    }
}
