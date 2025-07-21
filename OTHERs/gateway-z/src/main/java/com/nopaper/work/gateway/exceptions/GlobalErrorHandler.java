/**
 * @package com.nopaper.work.gateway.exceptions -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 7:56:09 am
 * @git 
 */
package com.nopaper.work.gateway.exceptions;

import java.time.LocalDateTime;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopaper.work.gateway.records.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 
 */

@Component
@Order(-1) // Ensure this handler is prioritized over default handlers
@Slf4j
@RequiredArgsConstructor
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

	private final ObjectMapper objectMapper;

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		log.error("Exception occurred: {}", ex.getMessage(), ex);

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String errorCode = "GW-500";
		String message = "An internal server error occurred.";

		// Map specific exceptions to standard error codes and statuses
		if (ex instanceof ResponseStatusException rse) {
			status = (HttpStatus) rse.getStatusCode();
			errorCode = "GW-" + status.value();
			message = rse.getReason() != null ? rse.getReason() : status.getReasonPhrase();
		} else if (ex instanceof SecurityException) { // Example of a custom exception
			status = HttpStatus.UNAUTHORIZED;
			errorCode = "GW-401";
			message = "Authentication failed.";
		}

		// Build the standard error response
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), exchange.getRequest().getPath().value(),
				status.value(), errorCode, message);

		exchange.getResponse().setStatusCode(status);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

		DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
		try {
			byte[] errorBytes = objectMapper.writeValueAsBytes(errorResponse);
			DataBuffer dataBuffer = bufferFactory.wrap(errorBytes);
			return exchange.getResponse().writeWith(Mono.just(dataBuffer));
		} catch (JsonProcessingException e) {
			log.error("Error writing JSON error response", e);
			return exchange.getResponse().setComplete();
		}
	}
}