/**
 * @package com.nopaper.work.gateway.configuration -> gateway
 * @author saikatbarman
 * @date 2025 07-Jul-2025 11:41:32 pm
 * @git 
 */
package com.nopaper.work.gateway.configuration;

/**
 * 
 */

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nopaper.work.gateway.filters.RequestAndResponseLogGlobalFilter;
import com.nopaper.work.gateway.services.ApiRouteService;
import com.nopaper.work.gateway.services.impl.ApiPathRouteLocatorImpl;

@Configuration
public class GatewayConfiguration {

	private RequestAndResponseLogGlobalFilter requestAndResponseLogGlobalFilter;

	@Bean
	public RouteLocator routeLocator(ApiRouteService apiRouteService, RouteLocatorBuilder routeLocatorBuilder) {
		return new ApiPathRouteLocatorImpl(apiRouteService, routeLocatorBuilder);
	}
}