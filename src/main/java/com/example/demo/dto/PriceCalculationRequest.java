package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceCalculationRequest {

    @Min(0)
    @Builder.Default
    private Integer demandPercentage = 0;

    private Integer hour;
}