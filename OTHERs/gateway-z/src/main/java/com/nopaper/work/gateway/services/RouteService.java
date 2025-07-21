/**
 * @package com.nopaper.work.gateway.services -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 10:46:38 am
 * @git 
 */
package com.nopaper.work.gateway.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.nopaper.work.gateway.models.Routes;
import com.nopaper.work.gateway.repository.RouteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * 
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

	private final RouteRepository routeRepository;

	@Cacheable("routes")
	public Flux<Routes> getActiveRoutes() {
		log.info("Fetching active routes from the database. (This should not appear frequently if caching is working)");
		return routeRepository.findAll();
	}
}