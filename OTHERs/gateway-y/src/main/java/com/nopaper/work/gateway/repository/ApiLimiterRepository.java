/**
 * @package com.nopaper.work.gateway.repository -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:15:31 am
 * @git 
 */
package com.nopaper.work.gateway.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.nopaper.work.gateway.models.ApiLimiter;
import com.nopaper.work.gateway.repository.custom.ApiLimiterCustomRepository;

public interface ApiLimiterRepository extends ReactiveCrudRepository<ApiLimiter, Long>, ApiLimiterCustomRepository {
}