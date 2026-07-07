package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dynamic_price", indexes = { @Index(name = "idx_product_timestamp", columnList = "product_id, timestamp")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DynamicPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "final_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal finalPrice;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}