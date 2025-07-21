/**
 * @package com.nopaper.work.gateway.services.impl -> gateway
 * @author saikatbarman
 * @date 2025 08-Jul-2025 12:48:41 am
 * @git 
 */
package com.nopaper.work.gateway.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.nopaper.work.gateway.services.GatewayRouteService;

/**
 * 
 */

@Service
public class GatewayRouteServiceImpl implements GatewayRouteService {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void refreshRoutes() {
		applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
	}
}