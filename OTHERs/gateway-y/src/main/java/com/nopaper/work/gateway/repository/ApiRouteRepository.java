/**
 * @package com.nopaper.work.gateway.repository -> gateway
 * @author saikatbarman
 * @date 2025 07-Jul-2025 11:33:56 pm
 * @git 
 */
package com.nopaper.work.gateway.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.nopaper.work.gateway.models.ApiRoute;

/**
 * 
 */

public interface ApiRouteRepository extends ReactiveCrudRepository<ApiRoute, Long> {
	  
}