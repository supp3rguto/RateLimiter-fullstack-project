package com.backend.ratelimiter.aop;

import com.backend.ratelimiter.exception.ApiKeyInvalidException;
import com.backend.ratelimiter.exception.RateLimitException;
import com.backend.ratelimiter.service.RateLimitingService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RateLimiterAspect {

    @Autowired
    private RateLimitingService rateLimitingService;

    // A assinatura do @Before mudou para "capturar" a anotação e ler seus parâmetros
    @Before("@annotation(rateLimited)")
    public void beforeRateLimitedMethod(RateLimited rateLimited) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String apiKey = request.getHeader("X-API-KEY");

        // Pega o nome do endpoint de dentro da anotação
        String endpointName = rateLimited.endpointName();

        if (apiKey == null || apiKey.isEmpty()) {
            // Lançamos uma exceção diferente para chave faltando
            throw new ApiKeyInvalidException("Header X-API-KEY está faltando.");
        }

        // Passamos o apiKey E o nome do endpoint para o serviço
        if (!rateLimitingService.tryConsumeToken(apiKey, endpointName)) {
            // Se o serviço retornar "false" (sem tokens), lança a exceção de limite
            throw new RateLimitException("Limite de requisições excedido.");
        }
    }
}