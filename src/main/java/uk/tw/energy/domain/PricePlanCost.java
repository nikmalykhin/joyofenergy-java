package uk.tw.energy.domain;

import java.math.BigDecimal;

public class PricePlanCost {
    private final String planName;
    private final BigDecimal cost;

    public PricePlanCost(String planName, BigDecimal cost) {
        this.planName = planName;
        this.cost = cost;
    }

    public String getPlanName() {
        return planName;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
