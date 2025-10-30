package com.backend.ratelimiter.service;

import com.backend.ratelimiter.exception.ApiKeyInvalidException;
import com.backend.ratelimiter.model.ApiKey;
import com.backend.ratelimiter.model.Plan;
import com.backend.ratelimiter.repository.ApiKeyRepository;
import com.backend.ratelimiter.repository.EndpointCostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RateLimitingService {

    // Não temos mais regras estáticas (constantes)

    @Autowired
    private ApiKeyRepository apiKeyRepository; // Novo

    @Autowired
    private EndpointCostRepository endpointCostRepository; // Novo

    @Transactional
    public boolean tryConsumeToken(String apiKeyId, String endpointName) {
        long now = System.currentTimeMillis();

        // 1 - busca o custo do endpoint no banco e se não achar o custo padrão é 1
        int cost = endpointCostRepository.findByEndpointName(endpointName)
                .map(endpoint -> endpoint.getTokenCost())
                .orElse(1);

        // 2 - busca a chave do usuário no banco e se não achar lança um erro 401
        ApiKey key = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ApiKeyInvalidException("API Key inválida."));

        // 3 - pega as regras dinamicas de dentro do Plano da chave
        Plan plan = key.getPlan();
        int capacity = plan.getBucketCapacity();
        long refillPeriodMs = plan.getRefillRateSeconds() * 1000L;

        // 4 - logica de refill (a mesma de antes mas usando as regras do plan)
        long timeElapsed = now - key.getLastRefillTimestamp();
        if (timeElapsed >= refillPeriodMs) {
            key.setTokens(capacity);
            key.setLastRefillTimestamp(now);
        }

        // 5 - logica de consumo, com custo dinâmico)
        if (key.getTokens() >= cost) {
            key.setTokens(key.getTokens() - cost);
            apiKeyRepository.save(key);
            return true;
        } else {
            apiKeyRepository.save(key);
            return false;
        }
    }
}