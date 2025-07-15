/**
 * @package com.nopaper.work.gateway.configurations -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 11:36:20 pm
 * @git 
 */
package com.nopaper.work.gateway.configurations;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

/**
 * 
 */

@Configuration
public class CacheConfig {

	/**
	 * This bean customizes the RedisCacheManager to use JSON serialization and sets
	 * a default Time-To-Live (TTL) for cache entries.
	 */
	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
		return (builder) -> builder.withCacheConfiguration("routes",
				org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
						// Set the cache entry to expire after 5 minutes
						.entryTtl(Duration.ofMinutes(5))
						// Use Jackson for JSON serialization
						.serializeValuesWith(
								SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())));
	}
}