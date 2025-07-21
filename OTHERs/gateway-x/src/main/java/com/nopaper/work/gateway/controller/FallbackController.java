/**
 * @package com.nopaper.work.gateway.filter -> gateway
 * @author saikatbarman
 * @date 2025 01-Jul-2025 12:06:17 am
 * @git 
 */
package com.nopaper.work.gateway.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */

@RestController
public class FallbackController {
	@GetMapping("/fallback/end-point")
	public ResponseEntity<List<String>> generalFallback() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Collections.singletonList("Service is unavailable, please try after sometime"));
	}
}