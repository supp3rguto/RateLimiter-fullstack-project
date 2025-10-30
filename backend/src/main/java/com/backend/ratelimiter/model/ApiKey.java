package com.backend.ratelimiter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Column;

@Entity
@Table(name = "api_keys")
@Data
public class ApiKey {

    @Id
    @Column(name = "api_key")
    private String apiKey;

    @Column(nullable = false)
    private String userId;

    @ManyToOne // muitas chaves podem pertencer a um plano
    @JoinColumn(name = "plan_id", nullable = false) // chave estrangeira
    private Plan plan;

    @Column(nullable = false)
    private int tokens;

    @Column(nullable = false)
    private long lastRefillTimestamp;
}