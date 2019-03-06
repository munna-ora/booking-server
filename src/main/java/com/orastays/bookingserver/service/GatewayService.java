package com.orastays.bookingserver.service;

import com.orastays.bookingserver.entity.GatewayEntity;

public interface GatewayService {
	GatewayEntity getGatewayEntity(String gatewayName);
}
