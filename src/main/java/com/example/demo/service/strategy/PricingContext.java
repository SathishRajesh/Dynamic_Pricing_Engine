package com.example.demo.service.strategy;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingContext {
    private Integer demandPercentage;
    private Integer hour;
    private Integer stockQuantity;
}