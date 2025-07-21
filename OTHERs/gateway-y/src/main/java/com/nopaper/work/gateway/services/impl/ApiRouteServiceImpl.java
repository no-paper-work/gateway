/**
 * @package com.nopaper.work.gateway.services.impl -> gateway
 * @author saikatbarman
 * @date 2025 07-Jul-2025 11:37:16 pm
 * @git 
 */
package com.nopaper.work.gateway.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nopaper.work.gateway.dto.CreateOrUpdateApiRouteRequest;
import com.nopaper.work.gateway.models.ApiRoute;
import com.nopaper.work.gateway.repository.ApiRouteRepository;
import com.nopaper.work.gateway.services.ApiRouteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 */

@Service
public class ApiRouteServiceImpl implements ApiRouteService {

	@Autowired
	private ApiRouteRepository apiRouteRepository;

	@Override
	public Flux<ApiRoute> findApiRoutes() {
		return apiRouteRepository.findAll();
	}

	@Override
	public Mono<ApiRoute> findApiRoute(Long id) {
		return findAndValidateApiRoute(id);
	}

	@Override
	public Mono<Void> createApiRoute(CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
		ApiRoute apiRoute = setNewApiRoute(new ApiRoute(), createOrUpdateApiRouteRequest);
		return apiRouteRepository.save(apiRoute).then();
	}

	@Override
	public Mono<Void> updateApiRoute(Long id, CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
		return findAndValidateApiRoute(id).map(apiRoute -> setNewApiRoute(apiRoute, createOrUpdateApiRouteRequest))
				.flatMap(apiRouteRepository::save).then();
	}

	@Override
	public Mono<Void> deleteApiRoute(Long id) {
		return findAndValidateApiRoute(id).flatMap(apiRoute -> apiRouteRepository.deleteById(apiRoute.getId()));
	}

	private Mono<ApiRoute> findAndValidateApiRoute(Long id) {
		return apiRouteRepository.findById(id)
				.switchIfEmpty(Mono.error(new RuntimeException(String.format("Api route with id %d not found", id))));
	}

	private ApiRoute setNewApiRoute(ApiRoute apiRoute, CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
		apiRoute.setPath(createOrUpdateApiRouteRequest.getPath());
		apiRoute.setMethod(createOrUpdateApiRouteRequest.getMethod());
		apiRoute.setUri(createOrUpdateApiRouteRequest.getUri());
		return apiRoute;
	}

}