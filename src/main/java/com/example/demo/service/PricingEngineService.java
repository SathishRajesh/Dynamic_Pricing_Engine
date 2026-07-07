package com.example.demo.service;

import com.example.demo.dto.PriceCalculationRequest;
import com.example.demo.dto.PriceResponse;
import com.example.demo.entity.DynamicPrice;
import com.example.demo.entity.PricingRule;
import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.DynamicPriceRepository;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.strategy.PricingContext;
import com.example.demo.service.strategy.PricingStrategy;
import com.example.demo.service.strategy.PricingStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingEngineService {

    private static final BigDecimal MAX_MULTIPLIER = BigDecimal.valueOf(3);

    private final ProductRepository productRepository;
    private final PricingRuleRepository pricingRuleRepository;
    private final DynamicPriceRepository dynamicPriceRepository;
    private final PricingStrategyFactory strategyFactory;

    @Cacheable(value = "productPrices",
            key = "#productId + '-' + #request.demandPercentage + '-' + #request.hour")
    @Transactional
    public PriceResponse calculatePrice(Long productId, PriceCalculationRequest request) {

        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        int hour = request.getHour() != null ? request.getHour() : LocalDateTime.now().getHour();

        PricingContext context = PricingContext.builder()
                .demandPercentage(request.getDemandPercentage())
                .hour(hour)
                .stockQuantity(product.getStockQuantity())
                .build();

        List<PricingRule> rules = pricingRuleRepository.findAllActiveOrderByPriorityDesc();

        BigDecimal finalPrice = product.getBasePrice();
        List<String> appliedRules = new ArrayList<>();

        for (PricingRule rule : rules) {
            PricingStrategy strategy = strategyFactory.getStrategy(rule.getType());
            if (strategy.isApplicable(rule, context)) {
                finalPrice = strategy.apply(finalPrice, rule, context);
                appliedRules.add(rule.getType() + " (rule #" + rule.getId() + ", x" + rule.getValue() + ")");
            }
        }

        BigDecimal maxAllowedPrice = product.getBasePrice().multiply(MAX_MULTIPLIER);
        if (finalPrice.compareTo(maxAllowedPrice) > 0) {
            finalPrice = maxAllowedPrice.setScale(2, RoundingMode.HALF_UP);
            appliedRules.add("CAP APPLIED (max 3x base price)");
        }

        DynamicPrice snapshot = DynamicPrice.builder()
                .product(product)
                .finalPrice(finalPrice)
                .timestamp(LocalDateTime.now())
                .build();
        dynamicPriceRepository.save(snapshot);

        return PriceResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .basePrice(product.getBasePrice())
                .finalPrice(finalPrice)
                .appliedRules(appliedRules)
                .calculatedAt(snapshot.getTimestamp())
                .build();
    }
}