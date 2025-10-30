package com.backend.ratelimiter.repository;

import com.backend.ratelimiter.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {
}