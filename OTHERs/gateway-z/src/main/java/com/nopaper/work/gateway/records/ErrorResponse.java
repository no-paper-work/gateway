/**
 * @package com.nopaper.work.gateway.dto -> gateway
 * @author saikatbarman
 * @date 2025 14-Jul-2025 7:55:01 am
 * @git 
 */
package com.nopaper.work.gateway.records;

import java.time.LocalDateTime;

/**
 * 
 */

public record ErrorResponse(LocalDateTime timestamp, String path, int httpStatus, String errorCode, String message) {
}