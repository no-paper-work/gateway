/**
 * @package com.nopaper.work.gateway.dto -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:40:19 am
 * @git 
 */
package com.nopaper.work.gateway.dto;

/**
 * 
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRateLimiterCacheDto {

	private String cacheKey;
	private long rate;
	private long remainingTtl;
	private int threshold;
	private int ttl;
}