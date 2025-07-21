/**
 * @package com.nopaper.work.gateway.models -> gateway
 * @author saikatbarman
 * @date 2025 08-Jul-2025 12:45:43 am
 * @git 
 */
package com.nopaper.work.gateway.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nopaper.work.gateway.constant.GatewayConstant;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrUpdateApiRouteRequest {
	@NotBlank
	private String path;
	private String method;
	@NotBlank
	private String uri;
	private Long id;
	private String status = GatewayConstant.DEFAULT_STATUS;
	private boolean isActive = GatewayConstant.IS_ACTIVE;
	private String abac;
	private String createdBy = GatewayConstant.DEFAULT_ACTOR;
	private Instant createdDate = Instant.now();
	private String lastModifiedBy = GatewayConstant.DEFAULT_ACTOR;
	private Instant lastModifiedDate = Instant.now();
}
