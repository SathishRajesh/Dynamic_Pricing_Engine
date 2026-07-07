package com.example.demo.dto;

import com.example.demo.entity.RuleType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePricingRuleRequest {

    @NotNull(message = "Rule type is required")
    private RuleType type;

    @NotNull(message = "Value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Value must be greater than 0")
    private BigDecimal value;

    @NotBlank(message = "Condition is required, e.g. 'demand>80' or 'hour>=18,hour<=22' or 'stock<10'")
    private String condition;

    @Min(value = 0, message = "Priority cannot be negative")
    private Integer priority = 0;
}