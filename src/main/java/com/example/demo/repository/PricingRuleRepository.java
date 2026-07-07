package com.example.demo.repository;

import com.example.demo.entity.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {

    @Query("SELECT r FROM PricingRule r WHERE r.active = true ORDER BY r.priority DESC")
    List<PricingRule> findAllActiveOrderByPriorityDesc();
}