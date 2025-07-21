/**
 * @package com.nopaper.work.gateway.services -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:22:21 am
 * @git 
 */
package com.nopaper.work.gateway.services;

import com.nopaper.work.gateway.models.ApiLimiter;
import com.nopaper.work.gateway.request.CreateOrUpdateApiLimiter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 */

public interface ApiLimiterService {

	Flux<ApiLimiter> findApiLimiters();

	Mono<ApiLimiter> findApiLimiter(Long id);

	Mono<Void> createApiLimiter(CreateOrUpdateApiLimiter createOrUpdateApiLimiter);

	Mono<Void> updateApiLimiter(Long id, CreateOrUpdateApiLimiter createOrUpdateApiLimiter);

	Mono<Void> deleteApiLimiter(Long id);

	Mono<Void> updateActivationStatus(Long id, boolean activate);

	Mono<ApiLimiter> findMatchesApiLimiter(String path, String method);
}