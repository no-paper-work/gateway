/**
 * @package com.nopaper.work.gateway.services.impl -> gateway
 * @author saikatbarman
 * @date 2025 07-Jul-2025 11:37:56 pm
 * @git 
 */
package com.nopaper.work.gateway.services.impl;

import lombok.AllArgsConstructor;

import java.util.Map;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.util.StringUtils;

import com.nopaper.work.gateway.models.ApiRoute;
import com.nopaper.work.gateway.services.ApiRouteService;

import reactor.core.publisher.Flux;

@AllArgsConstructor
public class ApiPathRouteLocatorImpl implements RouteLocator {

	private final ApiRouteService apiRouteService;

	private final RouteLocatorBuilder routeLocatorBuilder;

	@Override
	public Flux<Route> getRoutes() {
		RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();
		return apiRouteService.findApiRoutes()
				.map(apiRoute -> routesBuilder.route(String.valueOf(apiRoute.getId()),
						predicateSpec -> setPredicateSpec(apiRoute, predicateSpec)))
				.collectList().flatMapMany(builders -> routesBuilder.build().getRoutes());
	}

	private Buildable<Route> setPredicateSpec(ApiRoute apiRoute, PredicateSpec predicateSpec) {
		BooleanSpec booleanSpec = predicateSpec.path(apiRoute.getPath());
		if (!StringUtils.hasLength(apiRoute.getMethod())) { // replaced isEmpty(Object)
			booleanSpec.and().method(apiRoute.getMethod());
		}
		return booleanSpec.uri(apiRoute.getUri());
	}
	
	@Override
    public Flux<Route> getRoutesByMetadata(Map<String, Object> metadata) {
        return RouteLocator.super.getRoutesByMetadata(metadata);
    }
	
}