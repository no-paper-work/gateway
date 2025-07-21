/**
 * @package com.nopaper.work.gateway.filters -> gateway
 * @author saikatbarman
 * @date 2025 15-Jul-2025 11:02:06 pm
 * @git 
 */
package com.nopaper.work.gateway.filters;

import java.nio.charset.StandardCharsets;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * 
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopaper.work.gateway.records.RequestMetadata;
import com.nopaper.work.gateway.records.StandardizedRequest;
import com.nopaper.work.gateway.records.StandardizedResponse;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class TemplatingFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper; // Assuming you have an ObjectMapper bean

    @Override
    @SneakyThrows
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("TemplatingFilter: Applying standard templates.");

        // === REQUEST TRANSFORMATION ===
        ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            @SneakyThrows
            public Flux<DataBuffer> getBody() {
                
            	return DataBufferUtils.join(super.getBody())
                        .flatMap(dataBuffer -> {
                            byte[] bodyBytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bodyBytes);
                            DataBufferUtils.release(dataBuffer);
                            String payload = new String(bodyBytes, StandardCharsets.UTF_8);

                            String authority = exchange.getRequest().getHeaders().getFirst("X-User-Roles");
                            String requestId = (String) exchange.getAttributes().get("requestId");

                            RequestMetadata metadata = new RequestMetadata(requestId, authority, System.currentTimeMillis());
                            StandardizedRequest<?> standardizedRequest = new StandardizedRequest<>(metadata, payload);
                            
                            try {
                                byte[] newBodyBytes = objectMapper.writeValueAsBytes(standardizedRequest);
                                getHeaders().setContentLength(newBodyBytes.length);
                                return Mono.just(exchange.getResponse().bufferFactory().wrap(newBodyBytes));
                            } catch (Exception e) {
                                return Mono.error(new RuntimeException("Failed to serialize standardized request", e));
                            }
                        }).flux();
            	}
            	/* / This body is the DECRYPTED payload from the CryptoFilter
                return super.getBody().map(dataBuffer -> {
                    byte[] bodyBytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bodyBytes);
                    DataBufferUtils.release(dataBuffer);

                    // Assume the decrypted body is a JSON object that can be the 'payload'
                    String payload = new String(bodyBytes, StandardCharsets.UTF_8);

                    // Extract authority from a header (e.g., set by the Auth filter)
                    String authority = exchange.getRequest().getHeaders().getFirst("X-User-Roles");
                    String requestId = (String) exchange.getAttributes().get("requestId");

                    // Create the standardized request object
                    RequestMetadata metadata = new RequestMetadata(requestId, authority, System.currentTimeMillis());
                    StandardizedRequest<?> standardizedRequest = new StandardizedRequest<>(metadata, payload);

                    // Serialize the new standardized request to JSON bytes
                    byte[] newBodyBytes = null;
					try {
						newBodyBytes = objectMapper.writeValueAsBytes(standardizedRequest);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    // Update headers and return the new body
                    getHeaders().setContentLength(newBodyBytes.length);
                    return exchange.getResponse().bufferFactory().wrap(newBodyBytes);
                });
            } */
        };

        // === RESPONSE TRANSFORMATION ===
     // Response Transformation
        ServerHttpResponse decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                return DataBufferUtils.join(Flux.from(body))
                    .flatMap(dataBuffer -> {
                        byte[] bodyBytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bodyBytes);
                        DataBufferUtils.release(dataBuffer);
                        String serviceResponsePayload = new String(bodyBytes, StandardCharsets.UTF_8);

                        StandardizedResponse<?> standardizedResponse = new StandardizedResponse<>("SUCCESS", serviceResponsePayload, null);
                        
                        try {
                            byte[] newBodyBytes = objectMapper.writeValueAsBytes(standardizedResponse);
                            getHeaders().setContentLength(newBodyBytes.length);
                            DataBuffer buffer = getDelegate().bufferFactory().wrap(newBodyBytes);
                            return getDelegate().writeWith(Mono.just(buffer));
                        } catch (Exception e) {
                            log.error("Failed to serialize standardized response", e);
                            byte[] errorBytes = "{\"status\":\"ERROR\",\"data\":null,\"error\":{\"code\":\"500\",\"message\":\"Gateway Error\"}}".getBytes();
                            getHeaders().setContentLength(errorBytes.length);
                            DataBuffer buffer = getDelegate().bufferFactory().wrap(errorBytes);
                            return getDelegate().writeWith(Mono.just(buffer));
                        }
                    });
            }
        };
 /*       ServerHttpResponse decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                 // The body here is the PLAIN TEXT response from the downstream service
                 return Flux.from(body).map(dataBuffer -> {
                    byte[] bodyBytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bodyBytes);
                    DataBufferUtils.release(dataBuffer);
                    
                    String serviceResponsePayload = new String(bodyBytes, StandardCharsets.UTF_8);

                    // Assume the service response is the 'data' part of our standard response
                    // A real app would parse this and check for errors to populate the template correctly
                    StandardizedResponse<?> standardizedResponse = new StandardizedResponse<>("SUCCESS", serviceResponsePayload, null);
                    
                    byte[] newBodyBytes;
                    try {
                        newBodyBytes = objectMapper.writeValueAsBytes(standardizedResponse);
                    } catch (Exception e) {
                        // Handle serialization error
                        log.error("Failed to serialize standardized response", e);
                        newBodyBytes = "{\"status\":\"ERROR\",\"data\":null,\"error\":{\"code\":\"500\",\"message\":\"Gateway Error\"}}".getBytes();
                    }

                    // This new body will be passed to the CryptoFilter for ENCRYPTION
                    getHeaders().setContentLength(newBodyBytes.length);
                    return exchange.getResponse().bufferFactory().wrap(newBodyBytes);

                 }).collectList().flatMap(buffers -> {
                     DataBuffer mergedBuffer = exchange.getResponse().bufferFactory().join(buffers);
                     return getDelegate().writeWith(Mono.just(mergedBuffer));
                 });
            }
        }; */

        return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        // Must run AFTER CryptoFilter's request decryption (order 1)
        // and BEFORE CryptoFilter's response encryption.
        return 2;
    }
}