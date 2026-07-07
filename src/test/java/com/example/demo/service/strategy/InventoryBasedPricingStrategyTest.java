package com.example.demo.service.strategy;

import com.example.demo.entity.PricingRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InventoryBasedPricingStrategyTest {

    private final InventoryBasedPricingStrategy strategy = new InventoryBasedPricingStrategy();

    @Test
    void appliesWhenStockBelowThreshold() {
        PricingRule rule = PricingRule.builder().condition("stock<10").build();
        PricingContext context = PricingContext.builder().stockQuantity(5).build();

        assertTrue(strategy.isApplicable(rule, context));
    }

    @Test
    void doesNotApplyWhenStockAboveThreshold() {
        PricingRule rule = PricingRule.builder().condition("stock<10").build();
        PricingContext context = PricingContext.builder().stockQuantity(15).build();

        assertFalse(strategy.isApplicable(rule, context));
    }
}