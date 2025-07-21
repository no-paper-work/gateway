/**
 * @package com.nopaper.work.gateway.configurations -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 8:52:59 am
 * @git 
 */
package com.nopaper.work.gateway.configurations;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopaper.work.gateway.models.Routes;
import com.nopaper.work.gateway.services.RouteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class DynamicRouteLocator implements RouteDefinitionLocator {

	private final RouteService routeService;
	private final ObjectMapper objectMapper;

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		return routeService.getActiveRoutes().flatMap(this::convertRouteToDefinition);
	}

	private Mono<RouteDefinition> convertRouteToDefinition(Routes route) {
		try {
			RouteDefinition routeDefinition = new RouteDefinition();
			routeDefinition.setId(route.getRouteId());
			routeDefinition.setUri(new URI(route.getUri()));

			// Parse predicates
			List<PredicateDefinition> predicates = objectMapper.readValue(route.getPredicates(), new TypeReference<>() {});
			routeDefinition.setPredicates(predicates);

			// Parse filters and add rate limiter if configured
			List<FilterDefinition> filters = new ArrayList<>();
			if (StringUtils.hasText(route.getFilters())) {
				filters.addAll(objectMapper.readValue(route.getFilters(), new TypeReference<>() {}));
			}

			// ** Add Rate Limiter Filter dynamically **
			if (route.getRateLimitReplenishRate() != null && route.getRateLimitBurstCapacity() != null)
			{
				FilterDefinition rateLimiterFilter = new FilterDefinition("RequestRateLimiter");
				rateLimiterFilter.setArgs(Map.of(
						"key-resolver", "#{@ipKeyResolver}", // SpEL expression to reference our bean
						"redis-rate-limiter.replenishRate", String.valueOf(route.getRateLimitReplenishRate()),
						"redis-rate-limiter.burstCapacity", String.valueOf(route.getRateLimitBurstCapacity())
						));
				filters.add(rateLimiterFilter);
			}

			routeDefinition.setFilters(filters);
			log.debug("Loaded route [{}] with definition: {}", routeDefinition.getId(), routeDefinition);
			return Mono.just(routeDefinition);
		} catch (Exception e) {
			// A single mis-configured route should not stop the gateway
			log.error("Error creating route definition for route: {}. Skipping...", route.getRouteId(), e);
			return Mono.empty();
		}
	}
}