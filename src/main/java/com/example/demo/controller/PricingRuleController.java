package com.example.demo.controller;

import com.example.demo.dto.CreatePricingRuleRequest;
import com.example.demo.entity.PricingRule;
import com.example.demo.service.PricingRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rules")
@RequiredArgsConstructor
public class PricingRuleController {

    private final PricingRuleService pricingRuleService;

    @PostMapping
    public ResponseEntity<PricingRule> createRule(@Valid @RequestBody CreatePricingRuleRequest request) {
        PricingRule rule = pricingRuleService.createRule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(rule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PricingRule> updateRule(@PathVariable Long id,
                                                   @Valid @RequestBody CreatePricingRuleRequest request) {
        return ResponseEntity.ok(pricingRuleService.updateRule(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateRule(@PathVariable Long id) {
        pricingRuleService.deactivateRule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PricingRule>> getAllActiveRules() {
        return ResponseEntity.ok(pricingRuleService.getAllActiveRules());
    }
}