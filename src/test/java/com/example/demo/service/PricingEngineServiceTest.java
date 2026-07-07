package com.example.demo.service;

import com.example.demo.dto.PriceCalculationRequest;
import com.example.demo.dto.PriceResponse;
import com.example.demo.entity.Product;
import com.example.demo.repository.DynamicPriceRepository;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.strategy.PricingStrategyFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricingEngineServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private PricingRuleRepository pricingRuleRepository;
    @Mock private DynamicPriceRepository dynamicPriceRepository;
    @Mock private PricingStrategyFactory strategyFactory;

    @InjectMocks
    private PricingEngineService pricingEngineService;

    @Test
    void returnsBasePriceWhenNoRulesApply() {
        Product product = Product.builder()
                .id(1L).name("Test Product")
                .basePrice(BigDecimal.valueOf(2000))
                .stockQuantity(15)
                .build();

        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));
        when(pricingRuleRepository.findAllActiveOrderByPriorityDesc()).thenReturn(Collections.emptyList());

        PriceCalculationRequest request = PriceCalculationRequest.builder()
                .demandPercentage(10).hour(10).build();

        PriceResponse response = pricingEngineService.calculatePrice(1L, request);

        assertEquals(0, response.getFinalPrice().compareTo(BigDecimal.valueOf(2000).setScale(2)));
        assertTrue(response.getAppliedRules().isEmpty());
    }
}