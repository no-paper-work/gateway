/**
 * @package com.nopaper.work.gateway.services.impl -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:18:53 am
 * @git 
 */
package com.nopaper.work.gateway.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.nopaper.work.gateway.models.ApiLimiter;
import com.nopaper.work.gateway.repository.custom.ApiLimiterCustomRepository;

import reactor.core.publisher.Mono;

/**
 * 
 */

@Repository
public class ApiLimiterRepositoryImpl implements ApiLimiterCustomRepository {
	private static final String API_LIMITER_MATCH_REGEX_QUERY = 
			"SELECT * " 
					+ "FROM api_limiter apiLimiter "
					+ "WHERE apiLimiter.active = true AND :path SIMILAR TO apiLimiter.path "
					+ "AND apiLimiter.method = :method";

	@Autowired
	private DatabaseClient databaseClient;

	@Override
	public Mono<ApiLimiter> findMatchesApiLimiter(String path, String method) {
		return databaseClient.sql(API_LIMITER_MATCH_REGEX_QUERY)
				.bind("path", path)
				.bind("method", method)
				.map(row -> ApiLimiter.builder()
						.id(row.get("id", Long.class))
						.path(row.get("path", String.class))
						.method(row.get("method", String.class))
						.threshold(row.get("threshold", Integer.class))
						.ttl(row.get("ttl", Integer.class))
						.active(row.get("active", Boolean.class)).build())
				.first();
	}
}