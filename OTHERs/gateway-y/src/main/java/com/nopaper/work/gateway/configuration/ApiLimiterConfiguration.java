/**
 * @package com.nopaper.work.gateway.configuration -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 12:58:56 am
 * @git 
 */
package com.nopaper.work.gateway.configuration;

/**
 * 
 */

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

@Configuration
public class ApiLimiterConfiguration {
	@Bean
	public KeyResolver keyResolver() {
		return exchange -> Mono.just("1");
	}
}