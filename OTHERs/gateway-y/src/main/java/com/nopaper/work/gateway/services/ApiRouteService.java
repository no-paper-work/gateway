/**
 * @package com.nopaper.work.gateway.services -> gateway
 * @author saikatbarman
 * @date 2025 07-Jul-2025 11:36:45 pm
 * @git 
 */
package com.nopaper.work.gateway.services;

import com.nopaper.work.gateway.dto.CreateOrUpdateApiRouteRequest;
import com.nopaper.work.gateway.models.ApiRoute;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 */

public interface ApiRouteService {

	Flux<ApiRoute> findApiRoutes();

	Mono<ApiRoute> findApiRoute(Long id);

	Mono<Void> createApiRoute(CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest);

	Mono<Void> updateApiRoute(Long id, CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest);

	Mono<Void> deleteApiRoute(Long id);

}
