/**
 * @package com.nopaper.work.gateway.response -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:48:00 am
 * @git 
 */
package com.nopaper.work.gateway.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiLimiterResponse {

	private Long id;

	private String path;
	private String method;

	private int threshold;
	private int ttl;

	private boolean active;
}