/**
 * 
 */
package com.nopaper.work.gateway.configurations;

/**
 * 
 */

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	/**
	 * Creates a dedicated WebClient bean for making external calls that should not
	 * be subject to gateway load balancing logic. This prevents circular dependency
	 * issues on startup.
	 */
	@Bean
	@Qualifier("externalAuthWebClient")
	public WebClient externalAuthWebClient() {
		return WebClient.builder().build();
	}
}