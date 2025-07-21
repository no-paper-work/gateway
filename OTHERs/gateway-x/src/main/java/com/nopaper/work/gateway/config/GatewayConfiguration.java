/**
 * @package com.nopaper.work.gateway.config -> gateway
 * @author saikatbarman
 * @date 2025 04-Jul-2025 12:15:08 am
 * @git 
 */
package com.nopaper.work.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nopaper.work.gateway.filter.GatewayLoggerGlobalFilter;
import com.nopaper.work.gateway.service.RouteService;
import com.nopaper.work.gateway.service.impl.ApiRouteLocatorImpl;

/**
 * 
 */

@Configuration
public class GatewayConfiguration {

	private GatewayLoggerGlobalFilter gatewayLoggerGlobalFilter;
	
/*	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(10, 20, 1);
	}

	@Bean
	public KeyResolver hostNameKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
	}
*/
	/*
	 * spring: 
	 * 	cloud: 
	 * 		gateway: 
	 * 			routes: 
	 * 				- id: order-service ## Unique id of the route. This must be unique 
	 * 					uri: http://localhost:8081 ## Target microservice URI 
	 * 					predicates: ## condition that you want to check before routing to the given URI. 
	 * 					- Path=/orders/** ## Path predicate for URI path matching
	 * 
	 */

	/*
	 * 
	 * 
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder
				.routes()
				.route("product-service", routeDeclaration -> routeDeclaration
						.path("/api/products/**")
						.filters(filterDeclaration -> filterDeclaration
								.retry(retryConfig -> retryConfig.setRetries(10).setMethods(HttpMethod.GET))
								.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter()).setKeyResolver(hostNameKeyResolver()))
								.circuitBreaker(config -> config.setName("ecomBreaker").setFallbackUri("forward:/fallback/products"))
								.addRequestHeader("res-header", "res-header-value"))
//                        .filters(f -> f.rewritePath("/products(?<segment>/?.*)",
//                                "/api/products${segment}"))
						.uri("lb://PRODUCT-SERVICE"))
				
				.route("user-service", r -> r.path("/api/users/**")
//                        .filters(f -> f.rewritePath("/users(?<segment>/?.*)",
//                                "/api/users${segment}"))
						.uri("lb://USER-SERVICE"))
				
				.route("order-service", r -> r.path("/api/orders/**", "/api/cart/**")
//                        .filters(f -> f.rewritePath("/(?<segment>.*)",
//                                "/api/${segment}"))
						.uri("lb://ORDER-SERVICE"))
				
				.route("eureka-server",
						r -> r.path("/eureka/main").filters(f -> f.rewritePath("/eureka/main", "/"))
								.uri("http://localhost:8761"))
				
				.route("eureka-server-static", r -> r.path("/eureka/**").uri("http://localhost:8761"))
				
				.build();
	}
	*
	* 
	*/
	
	/*
	 * 
	 * @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes().route("order-service",
                        route -> route.path("/orders/**")
                                .filters(filter -> {
                                    filter.addResponseHeader("res-header", "res-header-value");
                                    return filter;
                                })
                                .uri("http://localhost:8081"))
                .build();
    }
	 */
	
    @Bean
    public RouteLocator routeLocator(RouteService routeService, RouteLocatorBuilder routeLocationBuilder) {
        return new ApiRouteLocatorImpl(routeLocationBuilder, routeService);
    }
}