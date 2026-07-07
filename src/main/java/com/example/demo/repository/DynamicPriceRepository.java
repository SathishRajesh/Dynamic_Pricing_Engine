package com.example.demo.repository;

import com.example.demo.entity.DynamicPrice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DynamicPriceRepository extends JpaRepository<DynamicPrice, Long> {

    @EntityGraph(attributePaths = "product")
    @Query("SELECT dp FROM DynamicPrice dp WHERE dp.product.id = :productId ORDER BY dp.timestamp DESC")
    List<DynamicPrice> findHistoryByProductId(Long productId);
}