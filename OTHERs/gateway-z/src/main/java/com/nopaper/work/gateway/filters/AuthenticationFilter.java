package com.nopaper.work.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final WebClient webClient; // MODIFIED: Changed from WebClient.Builder
    private final String authServiceUrl;

    // MODIFIED: Inject the specific "externalAuthWebClient" bean
    public AuthenticationFilter(
            @Qualifier("externalAuthWebClient") WebClient webClient,
            @Value("${gateway.auth.service-url}") String authServiceUrl){
        this.webClient = webClient;
        this.authServiceUrl = authServiceUrl;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().contains("/secure-login")) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            return unauthorized(exchange, "Authorization header is missing");
        }

        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Authorization header is invalid");
        }

        String token = authHeader.substring(7);

        // MODIFIED: Use the injected webClient directly
        return webClient.post().uri(authServiceUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity()
                .flatMap(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        String userId = response.getHeaders().getFirst("X-User-Id");
                        String userRoles = response.getHeaders().getFirst("X-User-Roles");

                        log.info("RequestId: [{}], Authentication successful for user: {}",
                                exchange.getAttribute("requestId"), userId);

                        ServerWebExchange modifiedExchange = exchange.mutate()
                                .request(r -> r
                                        .header("X-User-Id", userId)
                                        .header("X-User-Roles", userRoles)).build();
                        return chain.filter(modifiedExchange);
                    } else {
                        return unauthorized(exchange, "Token validation failed");
                    }
                }).onErrorResume(e -> {
                    log.error("RequestId: [{}], Error calling auth service: {}", exchange.getAttribute("requestId"),
                            e.getMessage());
                    return unauthorized(exchange, "Auth service is unavailable");
                });
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        log.warn("RequestId: [{}], Unauthorized access: {}", exchange.getAttribute("requestId"), message);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}