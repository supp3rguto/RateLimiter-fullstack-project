package com.backend.ratelimiter.repository;

import com.backend.ratelimiter.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}