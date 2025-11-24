package uk.tw.energy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;
import uk.tw.energy.domain.PricePlanCost;

@Service
public class PricePlanService {

    private final List<PricePlan> pricePlans;
    private final MeterReadingService meterReadingService;

    public PricePlanService(List<PricePlan> pricePlans, MeterReadingService meterReadingService) {
        this.pricePlans = pricePlans;
        this.meterReadingService = meterReadingService;
    }

    public Optional<Map<String, BigDecimal>> getConsumptionCostOfElectricityReadingsForEachPricePlan(
            String smartMeterId) {
        Optional<List<ElectricityReading>> electricityReadings = meterReadingService.getReadings(smartMeterId);

        if (!electricityReadings.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(pricePlans.stream()
                .collect(Collectors.toMap(PricePlan::getPlanName, t -> calculateCost(electricityReadings.get(), t))));
    }

    /**
     * Calculates the cost of electricity usage per PricePlan, grouped by DayOfWeek.
     */
    public Optional<Map<DayOfWeek, Map<String, BigDecimal>>> getCostPerPlanPerDayOfWeek(String smartMeterId) {
        Optional<List<ElectricityReading>> readingsOpt = meterReadingService.getReadings(smartMeterId);
        if (readingsOpt.isEmpty()) return Optional.empty();

        List<ElectricityReading> readings = readingsOpt.get();
        Map<DayOfWeek, List<ElectricityReading>> readingsByDay = groupReadingsByDayOfWeek(readings);

        Map<DayOfWeek, Map<String, BigDecimal>> result = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : readingsByDay.keySet()) {
            Map<String, BigDecimal> planCosts = new HashMap<>();
            for (PricePlan plan : pricePlans) {
                BigDecimal cost = calculateCostForDay(plan, readingsByDay.get(day), day);
                planCosts.put(plan.getPlanName(), cost);
            }
            result.put(day, planCosts);
        }
        return Optional.of(result);
    }

    /**
     * Ranks PricePlans by cost for each DayOfWeek, returning the lowest n plans per day.
     */
    public Optional<Map<DayOfWeek, List<PricePlanCost>>> rankLowestNPlansPerDayOfWeek(String smartMeterId, int n) {
        Optional<Map<DayOfWeek, Map<String, BigDecimal>>> costPerPlanPerDayOpt =
                getCostPerPlanPerDayOfWeek(smartMeterId);
        if (costPerPlanPerDayOpt.isEmpty()) return Optional.empty();

        Map<DayOfWeek, Map<String, BigDecimal>> costPerPlanPerDay = costPerPlanPerDayOpt.get();
        Map<DayOfWeek, List<PricePlanCost>> rankedPlans = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek day : costPerPlanPerDay.keySet()) {
            List<PricePlanCost> sorted = costPerPlanPerDay.get(day).entrySet().stream()
                    .map(e -> new PricePlanCost(e.getKey(), e.getValue()))
                    .sorted(Comparator.comparing(PricePlanCost::getCost))
                    .limit(n)
                    .collect(Collectors.toList());
            rankedPlans.put(day, sorted);
        }
        return Optional.of(rankedPlans);
    }

    // Helper: Group readings by DayOfWeek
    private Map<DayOfWeek, List<ElectricityReading>> groupReadingsByDayOfWeek(List<ElectricityReading> readings) {
        return readings.stream()
                .collect(Collectors.groupingBy(r -> ZonedDateTime.ofInstant(r.time(), ZoneId.systemDefault())
                        .getDayOfWeek()));
    }

    // Helper: Calculate cost for a given day
    private BigDecimal calculateCostForDay(PricePlan plan, List<ElectricityReading> readings, DayOfWeek day) {
        if (readings == null || readings.isEmpty()) return BigDecimal.ZERO;
        BigDecimal averageReading = calculateAverageReading(readings);
        BigDecimal usageTime = calculateUsageTimeInHours(readings);
        if (usageTime.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        BigDecimal energyConsumed = averageReading.divide(usageTime, RoundingMode.HALF_UP);
        BigDecimal multiplier = plan.getDayOfWeekMultiplier(day);
        return energyConsumed.multiply(plan.getUnitRate()).multiply(multiplier);
    }

    private BigDecimal calculateCost(List<ElectricityReading> electricityReadings, PricePlan pricePlan) {
        final BigDecimal averageReadingInKw = calculateAverageReading(electricityReadings);
        final BigDecimal usageTimeInHours = calculateUsageTimeInHours(electricityReadings);
        final BigDecimal energyConsumedInKwH = averageReadingInKw.divide(usageTimeInHours, RoundingMode.HALF_UP);
        final BigDecimal cost = energyConsumedInKwH.multiply(pricePlan.getUnitRate());
        return cost;
    }

    private BigDecimal calculateAverageReading(List<ElectricityReading> electricityReadings) {
        BigDecimal summedReadings =
                electricityReadings.stream().map(ElectricityReading::reading).reduce(BigDecimal.ZERO, BigDecimal::add);

        return summedReadings.divide(BigDecimal.valueOf(electricityReadings.size()), RoundingMode.HALF_UP);
    }

    private BigDecimal calculateUsageTimeInHours(List<ElectricityReading> electricityReadings) {
        ElectricityReading first = electricityReadings.stream()
                .min(Comparator.comparing(ElectricityReading::time))
                .get();

        ElectricityReading last = electricityReadings.stream()
                .max(Comparator.comparing(ElectricityReading::time))
                .get();

        return BigDecimal.valueOf(
                (double) (java.time.Duration.between(first.time(), last.time()).getSeconds()) / 3600.0);
    }
}
