/**
 * @package com.nopaper.work.gateway.controller -> gateway
 * @author saikatbarman
 * @date 2025 08-Jul-2025 12:43:15 am
 * @git 
 */
package com.nopaper.work.gateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nopaper.work.gateway.constant.ApiPath;
import com.nopaper.work.gateway.dto.ApiRouteResponse;
import com.nopaper.work.gateway.dto.CreateOrUpdateApiRouteRequest;
import com.nopaper.work.gateway.models.ApiRoute;
import com.nopaper.work.gateway.services.ApiRouteService;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 
 */

@RestController
@RequestMapping(path = ApiPath.INTERNAL_API_ROUTES)
public class InternalApiRouteController {

	@Autowired
	private ApiRouteService apiRouteService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<List<ApiRouteResponse>> findApiRoutes() {
		System.out.println("REACHED Controller");
		return apiRouteService.findApiRoutes().map(this::convertApiRouteIntoApiRouteResponse).collectList()
				.subscribeOn(Schedulers.boundedElastic());
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ApiRouteResponse> findApiRoute(@PathVariable Long id) {
		return apiRouteService.findApiRoute(id).map(this::convertApiRouteIntoApiRouteResponse)
				.subscribeOn(Schedulers.boundedElastic());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<?> createApiRoute(@RequestBody @Validated CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
		return apiRouteService.createApiRoute(createOrUpdateApiRouteRequest).subscribeOn(Schedulers.boundedElastic());
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<?> updateApiRoute(@PathVariable Long id,
			@RequestBody @Validated CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
		return apiRouteService.updateApiRoute(id, createOrUpdateApiRouteRequest)
				.subscribeOn(Schedulers.boundedElastic());
	}

	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<?> deleteApiRoute(@PathVariable Long id) {
		return apiRouteService.deleteApiRoute(id).subscribeOn(Schedulers.boundedElastic());
	}

	private ApiRouteResponse convertApiRouteIntoApiRouteResponse(ApiRoute apiRoute) {
		return ApiRouteResponse.builder().id(apiRoute.getId()).path(apiRoute.getPath()).method(apiRoute.getMethod())
				.uri(apiRoute.getUri()).build();
	}

}
