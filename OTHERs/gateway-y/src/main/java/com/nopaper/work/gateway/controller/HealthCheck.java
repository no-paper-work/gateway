/**
 * @package com.nopaper.work.gateway.controller -> gateway
 * @author saikatbarman
 * @date 2025 09-Jul-2025 6:58:24 pm
 * @git 
 */
package com.nopaper.work.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthCheck {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheck.class);

	@GetMapping("healthcheck")
	public String getHealthCheck() {
		return "200 ok";
	}

}