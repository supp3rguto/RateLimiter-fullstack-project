package com.backend.ratelimiter.repository;

import com.backend.ratelimiter.model.ApiLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLimitRepository extends JpaRepository<ApiLimit, String> {
}