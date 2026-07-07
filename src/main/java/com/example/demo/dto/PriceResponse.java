package com.example.demo.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceResponse {
    private Long productId;
    private String productName;
    private BigDecimal basePrice;
    private BigDecimal finalPrice;
    private List<String> appliedRules;
    private LocalDateTime calculatedAt;
}