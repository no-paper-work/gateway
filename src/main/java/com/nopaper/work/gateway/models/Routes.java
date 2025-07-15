/**
 * @package com.nopaper.work.gateway.models -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 8:16:27 am
 * @git 
 */
package com.nopaper.work.gateway.models;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.nopaper.work.gateway.models.audit.AbstractAuditEntity;

import io.lettuce.core.json.JsonType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "users", schema = "npw")
@EntityScan
public class Routes extends AbstractAuditEntity {

	private static final long serialVersionUID = -8705629358338932164L;

	@Id
	private Long id;

	@Column(value = "route_id")
	private String routeId;

	@Column(value = "uri")
	@NotNull
	private String uri;

	@Column(value = "predicates")
	private String predicates;

	@Column(value = "filters")
	private String filters;
	
	@Column(value = "metadata")
	@Type(JsonType.class)
	private Map<String, Object> metadata = new HashMap<>();

	@Column(value = "order")
	private int order = 0;

	@Column(value = "enabled")
	private boolean enabled = true;

	@Column(value = "rate_limit_replenish_rate")
	private Integer rateLimitReplenishRate; // Rate Limiting: Tokens per second

	@Column(value = "rate_limit_burst_capacity")
	private Integer rateLimitBurstCapacity; // Rate Limiting: Bucket capacity
	
}