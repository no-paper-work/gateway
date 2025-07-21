/**
 * @package com.nopaper.work.gateway.models.web -> gateway
 * @author saikatbarman
 * @date 2025 08-Jul-2025 12:46:43 am
 * @git 
 */
package com.nopaper.work.gateway.dto;

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
public class ApiRouteResponse {
	private Long id;
	private String path;
	private String method;
	private String uri;
	private String status;
	private boolean isActive;
	private String abac;
	
}