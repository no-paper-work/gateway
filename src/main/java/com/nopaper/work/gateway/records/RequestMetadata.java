/**
 * @package com.nopaper.work.gateway.dto -> gateway
 * @author saikatbarman
 * @date 2025 15-Jul-2025 11:09:42 pm
 * @git 
 */
package com.nopaper.work.gateway.records;

/**
 * 
 */

//Standard metadata for the request
public record RequestMetadata(String requestId, String authority, // e.g., 'user', 'admin', 'partner'
		long timestamp) {
}