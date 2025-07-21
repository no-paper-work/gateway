/**
 * @package com.nopaper.work.gateway.handler -> gateway
 * @author saikatbarman
 * @date 2025 05-Jul-2025 9:47:57 am
 * @git 
 */
package com.nopaper.work.gateway.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.util.UUID;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.nopaper.work.gateway.config.GatewayRoutesRefresher;
import com.nopaper.work.gateway.entity.ApiRoute;
import com.nopaper.work.gateway.service.RouteService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class ApiRouteHandler {
    private final RouteService routeService;

    private final RouteLocator routeLocator;

    private final GatewayRoutesRefresher gatewayRoutesRefresher;

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<ApiRoute> apiRoute = serverRequest.bodyToMono(ApiRoute.class);
        return apiRoute.flatMap(route ->
                ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(routeService.create(route), ApiRoute.class));
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        final UUID apiId = UUID.fromString(serverRequest.pathVariable("routeId"));
        Mono<ApiRoute> apiRoute = routeService.getById(apiId);
        return apiRoute.flatMap(route -> ServerResponse.ok()
                        .body(fromValue(route)))
                .switchIfEmpty(ServerResponse.notFound()
                        .build());
    }

	public Mono<ServerResponse> refreshRoutes(ServerRequest serverRequest) {
        gatewayRoutesRefresher.refreshRoutes();
        return ServerResponse.ok().body(BodyInserters.fromValue("Routes reloaded successfully"));
    }
}