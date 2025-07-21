/**
 * @package com.nopaper.work.gateway.filter -> gateway
 * @author saikatbarman
 * @date 2025 05-Jul-2025 1:02:28 am
 * @git 
 */
package com.nopaper.work.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 */

@Component
public class GatewayLoggerGlobalFilter implements GlobalFilter {

	private static final Logger logger = LoggerFactory.getLogger(GatewayLoggerGlobalFilter.class);

    /*
	@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Incoming request to: {}", exchange.getRequest().getPath());
        return chain.filter(exchange);
    }
    */
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Pre Filter Logic: Request path: {}", exchange.getRequest().getPath().toString());
        return chain
        		.filter(exchange)
        		.then(Mono.fromRunnable(() -> {
            logger.info("Post Filter Logic: HTTP Status Code: {}", exchange.getResponse().getStatusCode().toString());
        }));
    }

}
