/**
 * @package com.nopaper.work.gateway.filters -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 11:23:07 am
 * @git 
 */
package com.nopaper.work.gateway.filters;

import java.nio.charset.StandardCharsets;

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

import com.nopaper.work.gateway.utility.CryptoService;

/**
 * 
 */

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class CryptoFilter implements GlobalFilter, Ordered {

	private final CryptoService cryptoService;
	private static final String ENCRYPTION_HEADER = "X-Content-Encrypted";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("CryptoFilter: Intercepting request for potential decryption/encryption.");

		// Decorate the request to decrypt the body
		ServerHttpRequest request = exchange.getRequest();

// 1. Check if the encryption header is present and true
		boolean isEncryptionRequired = "true".equalsIgnoreCase(request.getHeaders().getFirst(ENCRYPTION_HEADER));

		if (!isEncryptionRequired) {
// 2. If not required, skip all crypto logic and pass the request down the chain.
			log.info("CryptoFilter: Skipping encryption/decryption as per header.");
			return chain.filter(exchange);
		}

		log.info("CryptoFilter: Applying encryption/decryption as per header.");

// 3. If required, proceed with the existing decorator logic. 
		// Decorate the request to decrypt the body
		ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(request) {
			@Override
			public Flux<DataBuffer> getBody() {
				return super.getBody().collectList().flatMap(dataBuffers -> {
					byte[] bodyBytes = new byte[dataBuffers.stream().mapToInt(DataBuffer::readableByteCount).sum()];
					int offset = 0;
					for (DataBuffer dataBuffer : dataBuffers) {
						int length = dataBuffer.readableByteCount();
						dataBuffer.read(bodyBytes, offset, length);
						offset += length;
						DataBufferUtils.release(dataBuffer);
					}
					String encryptedBody = new String(bodyBytes, StandardCharsets.UTF_8);
					// Decrypt and create a new body
					return cryptoService.decrypt(encryptedBody).map(decryptedBody -> {
						byte[] decryptedBytes = decryptedBody.getBytes(StandardCharsets.UTF_8);
						// Update content length header
						getHeaders().setContentLength(decryptedBytes.length);
						return exchange.getResponse().bufferFactory().wrap(decryptedBytes);
					});
				}).flux();
			}
		};

		ServerHttpResponse decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
			@Override
			public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
				Flux<DataBuffer> fluxBody = Flux.from(body);

				return fluxBody.collectList().flatMap(dataBuffers -> {
					byte[] bodyBytes = new byte[dataBuffers.stream().mapToInt(DataBuffer::readableByteCount).sum()];
					int offset = 0;
					for (DataBuffer dataBuffer : dataBuffers) {
						int length = dataBuffer.readableByteCount();
						dataBuffer.read(bodyBytes, offset, length);
						offset += length;
						DataBufferUtils.release(dataBuffer);
					}
					String plainTextBody = new String(bodyBytes, StandardCharsets.UTF_8);
					// Encrypt and create a new body
					return cryptoService.encrypt(plainTextBody).flatMap(encryptedBody -> {
						byte[] encryptedBytes = encryptedBody.getBytes(StandardCharsets.UTF_8);
						// Update content length header
						getHeaders().setContentLength(encryptedBytes.length);
						DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(encryptedBytes);
						return getDelegate().writeWith(Mono.just(buffer));
					});
				});
			}
		};

		return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build());
	}

	@Override
	public int getOrder() {
		// Should run after authentication but before routing filters.
		return 1;
	}
}