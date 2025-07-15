/**
 * @package com.nopaper.work.gateway.utility -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 11:59:05 am
 * @git 
 */
package com.nopaper.work.gateway.utility;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

/**
 * 
 */

@Service
public class CryptoService {

	// Placeholder for your decryption logic
	public Mono<String> decrypt(String encryptedBody) {
		// In a real application, you would use JCE, Bouncy Castle, etc.
		String decrypted = new StringBuilder(encryptedBody).reverse().toString();
		return Mono.just(decrypted);
	}

	// Placeholder for your encryption logic
	public Mono<String> encrypt(String plainTextBody) {
		// In a real application, this would be a robust encryption implementation
		String encrypted = new StringBuilder(plainTextBody).reverse().toString();
		return Mono.just(encrypted);
	}
}