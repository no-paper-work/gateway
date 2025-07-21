/**
 * @package com.nopaper.work.gateway.services.impl -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:23:53 am
 * @git 
 */
package com.nopaper.work.gateway.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nopaper.work.gateway.models.ApiLimiter;
import com.nopaper.work.gateway.repository.ApiLimiterRepository;
import com.nopaper.work.gateway.request.CreateOrUpdateApiLimiter;
import com.nopaper.work.gateway.services.ApiLimiterService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 */

@Service
public class ApiLimiterServiceImpl implements ApiLimiterService {

	@Autowired
	private ApiLimiterRepository apiLimiterRepository;

	@Override
	public Flux<ApiLimiter> findApiLimiters() {
		return apiLimiterRepository.findAll();
	}

	@Override
	public Mono<ApiLimiter> findApiLimiter(Long id) {
		return findAndValidateApiLimiter(id);
	}

	@Override
	public Mono<Void> createApiLimiter(CreateOrUpdateApiLimiter createOrUpdateApiLimiter) {
		ApiLimiter apiLimiter = setNewApiLimiter(new ApiLimiter(), createOrUpdateApiLimiter);
		return apiLimiterRepository.save(apiLimiter).then();
	}

	@Override
	public Mono<Void> updateApiLimiter(Long id, CreateOrUpdateApiLimiter createOrUpdateApiLimiter) {
		return findAndValidateApiLimiter(id).map(apiLimiter -> setNewApiLimiter(apiLimiter, createOrUpdateApiLimiter))
				.flatMap(apiLimiterRepository::save).then();
	}

	@Override
	public Mono<Void> deleteApiLimiter(Long id) {
		return findAndValidateApiLimiter(id).flatMap(apiLimiter -> apiLimiterRepository.deleteById(apiLimiter.getId()));
	}

	@Override
	public Mono<Void> updateActivationStatus(Long id, boolean activate) {
		return findAndValidateApiLimiter(id).doOnNext(apiLimiter -> apiLimiter.setActive(activate))
				.flatMap(apiLimiterRepository::save).then();
	}

	@Override
	public Mono<ApiLimiter> findMatchesApiLimiter(String path, String method) {
		return apiLimiterRepository.findMatchesApiLimiter(path, method);
	}

	private Mono<ApiLimiter> findAndValidateApiLimiter(Long id) {
		return apiLimiterRepository.findById(id)
				.switchIfEmpty(Mono.error(new RuntimeException(String.format("Api limiter with id %d not found", id))));
	}

	private ApiLimiter setNewApiLimiter(ApiLimiter apiLimiter, CreateOrUpdateApiLimiter createOrUpdateApiLimiter) {
		apiLimiter.setPath(createOrUpdateApiLimiter.getPath());
		apiLimiter.setMethod(createOrUpdateApiLimiter.getMethod());
		apiLimiter.setThreshold(createOrUpdateApiLimiter.getThreshold());
		apiLimiter.setTtl(createOrUpdateApiLimiter.getTtl());
		apiLimiter.setActive(createOrUpdateApiLimiter.isActive());
		return apiLimiter;
	}
}