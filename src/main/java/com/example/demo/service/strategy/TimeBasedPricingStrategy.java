package com.example.demo.service.strategy;

import com.example.demo.entity.PricingRule;
import com.example.demo.entity.RuleType;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TimeBasedPricingStrategy implements PricingStrategy {

    @Override
    public RuleType getSupportedType() {
        return RuleType.TIME_BASED;
    }

    @Override
    public boolean isApplicable(PricingRule rule, PricingContext context) {
        if (context.getHour() == null) return false;
        String[] parts = rule.getCondition().split(",");
        int start = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
        int end = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
        return context.getHour() >= start && context.getHour() <= end;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingRule rule, PricingContext context) {
        return currentPrice.multiply(rule.getValue()).setScale(2, RoundingMode.HALF_UP);
    }
}