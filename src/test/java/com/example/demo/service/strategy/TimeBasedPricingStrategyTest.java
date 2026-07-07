package com.example.demo.service.strategy;

import com.example.demo.entity.PricingRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeBasedPricingStrategyTest {

    private final TimeBasedPricingStrategy strategy = new TimeBasedPricingStrategy();

    @Test
    void appliesWhenHourInRange() {
        PricingRule rule = PricingRule.builder().condition("hour>=18,hour<=22").build();
        PricingContext context = PricingContext.builder().hour(19).build();

        assertTrue(strategy.isApplicable(rule, context));
    }

    @Test
    void doesNotApplyWhenHourOutOfRange() {
        PricingRule rule = PricingRule.builder().condition("hour>=18,hour<=22").build();
        PricingContext context = PricingContext.builder().hour(10).build();

        assertFalse(strategy.isApplicable(rule, context));
    }
}