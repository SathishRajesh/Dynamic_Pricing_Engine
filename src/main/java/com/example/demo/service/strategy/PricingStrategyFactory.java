package com.example.demo.service.strategy;

import com.example.demo.entity.RuleType;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PricingStrategyFactory {

    private final Map<RuleType, PricingStrategy> strategyMap;

    public PricingStrategyFactory(List<PricingStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(PricingStrategy::getSupportedType, s -> s));
    }

    public PricingStrategy getStrategy(RuleType type) {
        PricingStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for rule type: " + type);
        }
        return strategy;
    }
}