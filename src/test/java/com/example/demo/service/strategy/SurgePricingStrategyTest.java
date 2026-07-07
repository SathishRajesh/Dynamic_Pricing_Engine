package com.example.demo.service.strategy;

import com.example.demo.entity.PricingRule;
import com.example.demo.entity.RuleType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SurgePricingStrategyTest {

    private final SurgePricingStrategy strategy = new SurgePricingStrategy();

    @Test
    void appliesWhenDemandAboveThreshold() {
        PricingRule rule = PricingRule.builder()
                .type(RuleType.SURGE)
                .value(BigDecimal.valueOf(1.5))
                .condition("demand>80")
                .build();
        PricingContext context = PricingContext.builder().demandPercentage(90).build();

        assertTrue(strategy.isApplicable(rule, context));
    }

    @Test
    void doesNotApplyWhenDemandBelowThreshold() {
        PricingRule rule = PricingRule.builder()
                .type(RuleType.SURGE)
                .value(BigDecimal.valueOf(1.5))
                .condition("demand>80")
                .build();
        PricingContext context = PricingContext.builder().demandPercentage(50).build();

        assertFalse(strategy.isApplicable(rule, context));
    }

    @Test
    void multipliesCurrentPriceByRuleValue() {
        PricingRule rule = PricingRule.builder().value(BigDecimal.valueOf(1.5)).build();
        BigDecimal result = strategy.apply(BigDecimal.valueOf(2000), rule, new PricingContext());

        assertEquals(0, result.compareTo(BigDecimal.valueOf(3000).setScale(2)));
    }
}