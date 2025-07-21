/**
 * @package com.nopaper.work.gateway.service.impl -> gateway
 * @author saikatbarman
 * @date 2025 05-Jul-2025 2:00:07 am
 * @git 
 */
package com.nopaper.work.gateway.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nopaper.work.gateway.entity.ApiRoute;
import com.nopaper.work.gateway.repository.RouteRepository;
import com.nopaper.work.gateway.service.RouteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 */

@Service
public class RouteServiceImpl implements RouteService {
	
    @Autowired
	RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public Flux<ApiRoute> getAll() {
        return this.routeRepository.findAll();
    }

    @Override
    public Mono<ApiRoute> create(ApiRoute apiRoute) {
        Mono<ApiRoute> route = this.routeRepository.save(apiRoute);
        return route;
    }

    @Override
    public Mono<ApiRoute> getById(UUID id) {
        return this.routeRepository.findById(id);
    }
}