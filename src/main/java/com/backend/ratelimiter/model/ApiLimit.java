package com.backend.ratelimiter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ApiLimit {

    @Id
    private String apiKey;

    private int tokens;

    private long lastRefillTimestamp;

    public ApiLimit(String apiKey, int tokens, long lastRefillTimestamp) {
        this.apiKey = apiKey;
        this.tokens = tokens;
        this.lastRefillTimestamp = lastRefillTimestamp;
    }
}