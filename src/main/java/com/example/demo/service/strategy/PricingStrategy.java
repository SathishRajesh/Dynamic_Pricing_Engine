package com.example.demo.service.strategy;

import com.example.demo.entity.PricingRule;
import com.example.demo.entity.RuleType;
import java.math.BigDecimal;

public interface PricingStrategy {

    RuleType getSupportedType();

    boolean isApplicable(PricingRule rule, PricingContext context);

    BigDecimal apply(BigDecimal currentPrice, PricingRule rule, PricingContext context);
}