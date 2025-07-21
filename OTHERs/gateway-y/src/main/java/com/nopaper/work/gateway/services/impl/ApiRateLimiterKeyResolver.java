/**
 * @package com.nopaper.work.gateway.services.impl -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:28:57 am
 * @git 
 */
package com.nopaper.work.gateway.services.impl;

/**
 * 
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.nopaper.work.gateway.services.ApiLimiterService;
import com.nopaper.work.gateway.util.ObjectHelper;

import reactor.core.publisher.Mono;

@Service
public class ApiRateLimiterKeyResolver implements KeyResolver {

	@Autowired
	private ApiLimiterService apiLimiterService;

	@Autowired
	private ObjectHelper objectHelper;

	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
		return apiLimiterService
				.findMatchesApiLimiter(
						exchange
							.getRequest()
							.getPath()
							.value(), 
						exchange
							.getRequest()
							.getMethod().name())
				.doOnNext(apiLimiter -> 
						apiLimiter
							.setPath(exchange
									.getRequest()
									.getPath()
									.value()))
				.map(objectHelper::toStringBase64);
	}
}