/**
 * @package com.nopaper.work.gateway.models -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:04:42 am
 * @git 
 */
package com.nopaper.work.gateway.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.nopaper.work.gateway.constant.TableName;
import com.nopaper.work.gateway.models.audit.AbstractAuditEntity;

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
@Table(TableName.API_LIMITER) // To bind our model class with a database table with defined name
public class ApiLimiter extends AbstractAuditEntity{
	
  private static final long serialVersionUID = 5269467368224429651L;

  @Id // Indicating that this field is primary key in our database table
  private Long id;
  
  private String path;
  private String method;
  
  private int threshold;
  private int ttl;
  
  private boolean active;
  
}