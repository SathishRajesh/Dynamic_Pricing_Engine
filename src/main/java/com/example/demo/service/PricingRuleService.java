package com.example.demo.service;

import com.example.demo.dto.CreatePricingRuleRequest;
import com.example.demo.entity.PricingRule;
import com.example.demo.repository.PricingRuleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingRuleService {

    private final PricingRuleRepository pricingRuleRepository;

    // Any rule change must invalidate cached prices - stale prices would violate
    // the "Rule updates during processing" edge case requirement.
    @CacheEvict(value = "productPrices", allEntries = true)
    @Transactional
    public PricingRule createRule(CreatePricingRuleRequest request) {
        PricingRule rule = PricingRule.builder()
                .type(request.getType())
                .value(request.getValue())
                .condition(request.getCondition())
                .priority(request.getPriority())
                .active(true)
                .build();
        return pricingRuleRepository.save(rule);
    }

    @CacheEvict(value = "productPrices", allEntries = true)
    @Transactional
    public PricingRule updateRule(Long id, CreatePricingRuleRequest request) {
        PricingRule rule = pricingRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pricing rule not found: " + id));
        rule.setType(request.getType());
        rule.setValue(request.getValue());
        rule.setCondition(request.getCondition());
        rule.setPriority(request.getPriority());
        return pricingRuleRepository.save(rule);
    }

    @CacheEvict(value = "productPrices", allEntries = true)
    @Transactional
    public void deactivateRule(Long id) {
        PricingRule rule = pricingRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pricing rule not found: " + id));
        rule.setActive(false);
        pricingRuleRepository.save(rule);
    }

    @Transactional(readOnly = true)
    public List<PricingRule> getAllActiveRules() {
        return pricingRuleRepository.findAllActiveOrderByPriorityDesc();
    }
}