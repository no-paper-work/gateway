/**
 * @package com.nopaper.work.gateway.request -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:46:41 am
 * @git 
 */
package com.nopaper.work.gateway.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrUpdateApiLimiter {

  @NotBlank
  private String path;

  @NotBlank
  private String method;

  private int threshold;
  private int ttl;

  private boolean active;
}