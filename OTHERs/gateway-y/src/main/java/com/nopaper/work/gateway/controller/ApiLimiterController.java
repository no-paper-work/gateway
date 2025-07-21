/**
 * @package com.nopaper.work.gateway.controller -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:49:05 am
 * @git 
 */
package com.nopaper.work.gateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nopaper.work.gateway.constant.ApiPath;
import com.nopaper.work.gateway.models.ApiLimiter;
import com.nopaper.work.gateway.request.CreateOrUpdateApiLimiter;
import com.nopaper.work.gateway.response.ApiLimiterResponse;
import com.nopaper.work.gateway.services.ApiLimiterService;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 
 */

@RestController
@RequestMapping(path = ApiPath.INTERNAL_API_LIMITERS)
public class ApiLimiterController {
  
  @Autowired
  private ApiLimiterService apiLimiterService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<ApiLimiterResponse>> findApiLimiters() {
    return apiLimiterService.findApiLimiters()
        .map(this::convertApiLimiterIntoApiLimiterResponse)
        .collectList()
        .subscribeOn(Schedulers.boundedElastic());
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ApiLimiterResponse> findApiLimiter(@PathVariable Long id) {
    return apiLimiterService.findApiLimiter(id)
        .map(this::convertApiLimiterIntoApiLimiterResponse)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<?> createApiLimiter(
      @RequestBody @Validated CreateOrUpdateApiLimiter createOrUpdateApiLimiter) {
    return apiLimiterService.createApiLimiter(createOrUpdateApiLimiter)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @PutMapping(path = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<?> updateApiLimiter(@PathVariable Long id,
      @RequestBody @Validated CreateOrUpdateApiLimiter createOrUpdateApiLimiter) {
    return apiLimiterService.updateApiLimiter(id, createOrUpdateApiLimiter)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @DeleteMapping(path = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<?> deleteApiLimiter(@PathVariable Long id) {
    return apiLimiterService.deleteApiLimiter(id)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @PutMapping(path = "/{id}/activate",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<?> activateApiLimiter(@PathVariable Long id) {
    return apiLimiterService.updateActivationStatus(id, true)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @PutMapping(path = "/{id}/deactivate",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<?> deactivateApiLimiter(@PathVariable Long id) {
    return apiLimiterService.updateActivationStatus(id, false)
        .subscribeOn(Schedulers.boundedElastic());
  }

  private ApiLimiterResponse convertApiLimiterIntoApiLimiterResponse(ApiLimiter apiLimiter) {
    return ApiLimiterResponse.builder()
        .id(apiLimiter.getId())
        .path(apiLimiter.getPath())
        .method(apiLimiter.getMethod())
        .threshold(apiLimiter.getThreshold())
        .ttl(apiLimiter.getTtl())
        .active(apiLimiter.isActive())
        .build();
  }
}