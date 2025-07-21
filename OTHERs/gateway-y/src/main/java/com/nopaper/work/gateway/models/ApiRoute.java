/**
 * @package com.nopaper.work.gateway.models -> gateway
 * @author saikatbarman
 * @date 2025 07-Jul-2025 9:35:12 pm
 * @git 
 */
package com.nopaper.work.gateway.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.nopaper.work.gateway.constant.TableName;
import com.nopaper.work.gateway.models.audit.AbstractAuditEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 */

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.API_ROUTE, schema = "way") // To bind our model class with a database table with defined name
public class ApiRoute extends AbstractAuditEntity {

	private static final long serialVersionUID = 6382341640369602495L;

	@Id // Indicating that this field is primary key in our database table
	@Column(value = "id")
	private Long id;

	@Column(value = "path")
	private String path;

	@Column(value = "method")
	private String method;

	@Column(value = "uri")
	private String uri;

	/*
	 * private int order; 
	 * private AsyncPredicate<ServerWebExchange> predicate;
	 * private List<GatewayFilter> gatewayFilters; 
	 * private Map<String, Object> metadata;
	 * 
	 */

}