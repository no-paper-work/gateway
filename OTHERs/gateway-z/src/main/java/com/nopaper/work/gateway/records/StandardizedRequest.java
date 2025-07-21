/**
 * @package com.nopaper.work.gateway.dto -> gateway
 * @author saikatbarman
 * @date 2025 15-Jul-2025 11:09:02 pm
 * @git 
 */
package com.nopaper.work.gateway.records;

/**
 * 
 */

//Standard wrapper for requests sent to internal services
public record StandardizedRequest<T>(RequestMetadata metadata, T payload) {
}