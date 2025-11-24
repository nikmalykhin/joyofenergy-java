package uk.tw.energy.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;
import uk.tw.energy.domain.PricePlanCost;

class PricePlanServiceTest {

    private MeterReadingService meterReadingService;
    private PricePlanService pricePlanService;
    private static final String SMART_METER_ID = "smart-meter-id";

    @BeforeEach
    void setUp() {
        meterReadingService = new MeterReadingService(new HashMap<>());

        PricePlan planA = new PricePlan("plan-A", "supplier-A", BigDecimal.valueOf(2), null);
        PricePlan planB = new PricePlan("plan-B", "supplier-B", BigDecimal.valueOf(1), null);
        List<PricePlan> pricePlans = List.of(planA, planB);

        pricePlanService = new PricePlanService(pricePlans, meterReadingService);
    }

    @Test
    void getCostPerPlanPerDayOfWeek_returnsCostsGroupedByDay() {
        ElectricityReading reading1 =
                new ElectricityReading(Instant.parse("2023-11-20T10:00:00Z"), BigDecimal.valueOf(10));
        ElectricityReading reading2 =
                new ElectricityReading(Instant.parse("2023-11-21T10:00:00Z"), BigDecimal.valueOf(20));
        meterReadingService.storeReadings(SMART_METER_ID, List.of(reading1, reading2));

        var resultOpt = pricePlanService.getCostPerPlanPerDayOfWeek(SMART_METER_ID);

        assertThat(resultOpt).isPresent();
        Map<DayOfWeek, Map<String, BigDecimal>> result = resultOpt.get();
        assertThat(result).containsKeys(DayOfWeek.MONDAY, DayOfWeek.TUESDAY);
        assertThat(result.get(DayOfWeek.MONDAY)).containsKeys("plan-A", "plan-B");
        assertThat(result.get(DayOfWeek.TUESDAY)).containsKeys("plan-A", "plan-B");
    }

    @Test
    void rankLowestNPlansPerDayOfWeek_returnsRankedPlans() {
        ElectricityReading reading1 =
                new ElectricityReading(Instant.parse("2023-11-24T10:00:00Z"), BigDecimal.valueOf(5));
        ElectricityReading reading2 =
                new ElectricityReading(Instant.parse("2023-11-24T12:00:00Z"), BigDecimal.valueOf(15));
        meterReadingService.storeReadings(SMART_METER_ID, List.of(reading1, reading2));

        var resultOpt = pricePlanService.rankLowestNPlansPerDayOfWeek(SMART_METER_ID, 1);

        assertThat(resultOpt).isPresent();
        Map<DayOfWeek, List<PricePlanCost>> result = resultOpt.get();
        assertThat(result).containsKey(DayOfWeek.FRIDAY);
        List<PricePlanCost> ranked = result.get(DayOfWeek.FRIDAY);
        assertThat(ranked).hasSize(1);
        assertThat(ranked.get(0).getPlanName()).isIn("plan-A", "plan-B");
    }
}
