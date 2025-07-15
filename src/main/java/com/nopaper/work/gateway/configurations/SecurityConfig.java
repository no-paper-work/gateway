/**
 * @package com.nopaper.work.gateway.security -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 11:03:51 am
 * @git 
 * 
 * Security configuration for the API Gateway.
 * Configures CSRF, authorization, and security headers for all routes.
 * All exchanges are permitted, but further authentication is handled by filters.
 */

package com.nopaper.work.gateway.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .anyExchange().permitAll()
            )
            .headers(headers -> headers
                // This is the simplest and correct way to set X-Frame-Options to DENY
                .frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable)
            );
        return http.build();
    }
}