/**
 * @package com.nopaper.work.gateway.records -> gateway
 * @author saikatbarman
 * @date 2025 15-Jul-2025 11:10:29 pm
 * @git 
 */
package com.nopaper.work.gateway.records;

/**
 * 
 */

//Standard wrapper for responses sent back to the gateway
public record StandardizedResponse<T>(String status, T data, ErrorDetails error) {
}
