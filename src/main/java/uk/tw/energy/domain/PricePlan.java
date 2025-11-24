package uk.tw.energy.domain;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PricePlan {

    private final String energySupplier;
    private final String planName;
    private final BigDecimal unitRate; // unit price per kWh
    private final List<PeakTimeMultiplier> peakTimeMultipliers;
    private final Map<DayOfWeek, BigDecimal> dayOfWeekMultipliers;

    public PricePlan(
            String planName, String energySupplier, BigDecimal unitRate, List<PeakTimeMultiplier> peakTimeMultipliers) {
        this.planName = planName;
        this.energySupplier = energySupplier;
        this.unitRate = unitRate;
        this.peakTimeMultipliers = peakTimeMultipliers;
        this.dayOfWeekMultipliers = new HashMap<>();
    }

    public String getEnergySupplier() {
        return energySupplier;
    }

    public String getPlanName() {
        return planName;
    }

    public BigDecimal getUnitRate() {
        return unitRate;
    }

    public BigDecimal getPrice(LocalDateTime dateTime) {
        if (peakTimeMultipliers != null) {
            for (PeakTimeMultiplier ptm : peakTimeMultipliers) {
                if (ptm.getDayOfWeek().equals(dateTime.getDayOfWeek())) {
                    return ptm.getMultiplier();
                }
            }
        }
        return unitRate;
    }

    public BigDecimal getDayOfWeekMultiplier(DayOfWeek day) {
        if (peakTimeMultipliers == null) return BigDecimal.ONE;
        return peakTimeMultipliers.stream()
                .filter(ptm -> ptm.getDayOfWeek().equals(day))
                .map(PricePlan.PeakTimeMultiplier::getMultiplier)
                .findFirst()
                .orElse(BigDecimal.ONE);
    }

    static class PeakTimeMultiplier {

        DayOfWeek dayOfWeek;
        BigDecimal multiplier;

        public PeakTimeMultiplier(DayOfWeek dayOfWeek, BigDecimal multiplier) {
            this.dayOfWeek = dayOfWeek;
            this.multiplier = multiplier;
        }

        public DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        public BigDecimal getMultiplier() {
            return multiplier;
        }
    }
}
