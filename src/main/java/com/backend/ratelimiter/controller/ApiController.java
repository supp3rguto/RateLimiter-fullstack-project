package com.backend.ratelimiter.controller;

import com.backend.ratelimiter.aop.RateLimited;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        // obs: esse endpoint aqui não tem @RateLimited, é livre
        return ResponseEntity.ok("Endpoint público - OK: " + LocalTime.now());
    }

    @GetMapping("/limited")
    @RateLimited(endpointName = "get_limited_data")
    public ResponseEntity<String> limitedEndpoint() {
        // só executa se o RateLimiterAspect permitir
        return ResponseEntity.ok("Endpoint LIMITADO - SUCESSO: " + LocalTime.now());
    }
}