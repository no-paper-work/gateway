/**
 * @package com.nopaper.work.gateway.repository.custom -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:16:52 am
 * @git 
 */
package com.nopaper.work.gateway.repository.custom;

import com.nopaper.work.gateway.models.ApiLimiter;

import reactor.core.publisher.Mono;

/**
 * 
 */

public interface ApiLimiterCustomRepository {
	Mono<ApiLimiter> findMatchesApiLimiter(String path, String method);
}