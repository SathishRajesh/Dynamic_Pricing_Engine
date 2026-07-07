package com.example.demo.service.strategy;

import com.example.demo.entity.PricingRule;
import com.example.demo.entity.RuleType;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class InventoryBasedPricingStrategy implements PricingStrategy {

    @Override
    public RuleType getSupportedType() {
        return RuleType.INVENTORY;
    }

    @Override
    public boolean isApplicable(PricingRule rule, PricingContext context) {
        int threshold = extractThreshold(rule.getCondition());
        return context.getStockQuantity() != null
                && context.getStockQuantity() < threshold;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingRule rule, PricingContext context) {
        return currentPrice.multiply(rule.getValue()).setScale(2, RoundingMode.HALF_UP);
    }

    private int extractThreshold(String condition) {
        String digits = condition.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }
}