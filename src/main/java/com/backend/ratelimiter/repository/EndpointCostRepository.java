package com.backend.ratelimiter.repository;

import com.backend.ratelimiter.model.EndpointCost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EndpointCostRepository extends JpaRepository<EndpointCost, Long> {

    // Método mágico do Spring Data:
    // "Encontre-me um registro pela coluna 'endpointName'"
    Optional<EndpointCost> findByEndpointName(String endpointName);
}