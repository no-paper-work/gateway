/**
 * @package com.nopaper.work.gateway.repository -> gateway
 * @author saikatbarman
 * @date 2025 05-Jul-2025 1:57:31 am
 * @git 
 */
package com.nopaper.work.gateway.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.nopaper.work.gateway.entity.ApiRoute;

/**
 * 
 */

@Repository
public interface RouteRepository extends ReactiveCrudRepository<ApiRoute, UUID> {
}