/**
 * @package com.nopaper.work.gateway.configurations -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 10:35:22 am
 * @git 
 */
package com.nopaper.work.gateway.configurations;

import java.util.Objects;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

/**
 * 
 */

@Configuration
public class RateLimiterConfig {

	@Bean
	public KeyResolver ipKeyResolver() {
		return exchange -> Mono
				.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
	}
}