/**
 * @package com.nopaper.work.gateway.filters -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 11:44:11 pm
 * @git 
 */
package com.nopaper.work.gateway.filters;

import java.util.Objects;
import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class IpBlacklistFilter implements GlobalFilter, Ordered {

	// Inject the reactive Redis template
	private final ReactiveRedisTemplate<String, String> redisTemplate;
	private static final String IP_BLACKLIST_KEY = "ip_blacklist";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String clientIp = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress()
				.getHostAddress();

		// Check if the client IP is blacklisted in Redis
		return redisTemplate.opsForSet().isMember(IP_BLACKLIST_KEY, clientIp).flatMap(isBlacklisted -> {
			// 1. IP Blacklisting
			if (Boolean.TRUE.equals(isBlacklisted)) {
				// Block the request if IP is blacklisted
				log.warn("Blocked request from blacklisted IP in Redis: {}", clientIp);
				exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
				return exchange.getResponse().setComplete();
			}

			// If not blacklisted, continue with the logging and chain
			// Add a unique request ID for tracing
			String requestId = UUID.randomUUID().toString();
			exchange.getAttributes().put("requestId", requestId);
			long startTime = System.currentTimeMillis();
			// 2. Logging and Monitoring (Pre-flight)
			log.info("RequestId: [{}], Request In: {} {}, Client IP: {}", requestId, exchange.getRequest().getMethod(),
					exchange.getRequest().getURI(), clientIp);

			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				// 3. Performance Tracking (Post-flight)
				long duration = System.currentTimeMillis() - startTime;
				log.info("RequestId: [{}], Request Out: Status {}, Duration: {}ms", requestId,
						exchange.getResponse().getStatusCode(), duration);
			}));
		});
	}

	@Override
	public int getOrder() {
		return -1; // Run this filter first
	}
}