/**
 * @package com.nopaper.work.gateway.service -> gateway
 * @author saikatbarman
 * @date 2025 05-Jul-2025 1:59:42 am
 * @git 
 */
package com.nopaper.work.gateway.service;

import java.util.UUID;

import com.nopaper.work.gateway.entity.ApiRoute;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 */

public interface RouteService {
	Flux<ApiRoute> getAll();

	Mono<ApiRoute> create(ApiRoute apiRoute);

	Mono<ApiRoute> getById(UUID id);
}