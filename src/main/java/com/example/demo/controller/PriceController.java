package com.example.demo.controller;

import com.example.demo.dto.PriceCalculationRequest;
import com.example.demo.dto.PriceResponse;
import com.example.demo.service.PricingEngineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PricingEngineService pricingEngineService;

    @PostMapping("/{productId}/calculate")
    public ResponseEntity<PriceResponse> calculatePrice(@PathVariable Long productId,@Valid @RequestBody(required = false) PriceCalculationRequest request) {
        PriceCalculationRequest req = request != null ? request : new PriceCalculationRequest();
        return ResponseEntity.ok(pricingEngineService.calculatePrice(productId, req));
    }
}