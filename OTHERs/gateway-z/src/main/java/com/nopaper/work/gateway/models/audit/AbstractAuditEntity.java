/**
 * @package com.nopaper.work.gateway.models.audit -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 8:24:14 am
 * @git 
 */
package com.nopaper.work.gateway.models.audit;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.relational.core.mapping.Column;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import io.lettuce.core.json.JsonType;
import lombok.Data;

/**
 * 
 */
@Data
public abstract class AbstractAuditEntity implements Serializable {
	
	private static final long serialVersionUID = -2063567750470761954L;

	@Column(value = "status")
	private String status;

	@Column(value = "abac")
	@Type(JsonType.class)
	private String abac;

	@Column(value = "created_by")
	private String createdBy;

	@Column(value = "created_date")
	private Instant createdDate;

	@Column(value = "last_modified_by")
	private String lastModifiedBy;

	@Column(value = "last_modified_date")
	private Instant lastModifiedDate;
	
}