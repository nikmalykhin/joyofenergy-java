package uk.tw.energy.domain;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class Tariff {

    @NotNull(message = "Tariff Name is required.") @Size(min = 1, message = "Tariff Name is required.")
    private String tariffName;

    @NotNull(message = "Supplier Name is required.") @Size(min = 1, message = "Supplier Name is required.")
    private String supplierName;

    @NotNull(message = "Tariff Type is required.") private TariffType tariffType;

    // Only required for FLAT_RATE
    @DecimalMin(value = "0.0", inclusive = true, message = "Prices and charges cannot be negative.")
    private BigDecimal unitRateKwh;

    @NotNull(message = "Standing Charge per Day is required.") @DecimalMin(value = "0.0", inclusive = true, message = "Prices and charges cannot be negative.")
    private BigDecimal standingChargePerDay;

    @NotNull(message = "Valid From date is required.") private LocalDate validFrom;

    // Only required for TIME_OF_USE
    @Size(min = 24, max = 24, message = "24 hourly rates are required for TIME_OF_USE tariff.")
    private Map<
                    @NotNull Integer,
                    @NotNull @DecimalMin(value = "0.0", inclusive = true, message = "Prices and charges cannot be negative.")
                    BigDecimal>
            hourlyRates;

    public Tariff() {}

    public Tariff(
            String tariffName,
            String supplierName,
            TariffType tariffType,
            BigDecimal unitRateKwh,
            BigDecimal standingChargePerDay,
            LocalDate validFrom,
            Map<Integer, BigDecimal> hourlyRates) {
        this.tariffName = tariffName;
        this.supplierName = supplierName;
        this.tariffType = tariffType;
        this.unitRateKwh = unitRateKwh;
        this.standingChargePerDay = standingChargePerDay;
        this.validFrom = validFrom;
        this.hourlyRates = hourlyRates;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public TariffType getTariffType() {
        return tariffType;
    }

    public void setTariffType(TariffType tariffType) {
        this.tariffType = tariffType;
    }

    public BigDecimal getUnitRateKwh() {
        return unitRateKwh;
    }

    public void setUnitRateKwh(BigDecimal unitRateKwh) {
        this.unitRateKwh = unitRateKwh;
    }

    public BigDecimal getStandingChargePerDay() {
        return standingChargePerDay;
    }

    public void setStandingChargePerDay(BigDecimal standingChargePerDay) {
        this.standingChargePerDay = standingChargePerDay;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public Map<Integer, BigDecimal> getHourlyRates() {
        return hourlyRates;
    }

    public void setHourlyRates(Map<Integer, BigDecimal> hourlyRates) {
        this.hourlyRates = hourlyRates;
    }
}
