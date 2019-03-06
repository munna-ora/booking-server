package com.orastays.bookingserver.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.orastays.bookingserver.converter.BookingConverter;
import com.orastays.bookingserver.converter.BookingInfoConverter;
import com.orastays.bookingserver.converter.BookingVsRoomConverter;
import com.orastays.bookingserver.dao.BookingDAO;
import com.orastays.bookingserver.dao.BookingInfoDAO;
import com.orastays.bookingserver.dao.BookingVsRoomDAO;
import com.orastays.bookingserver.helper.CashFreeApi;
import com.orastays.bookingserver.service.GatewayService;
import com.orastays.bookingserver.service.SacService;

public class BaseUtil {
	@Autowired
	protected BookingConverter bookingConverter;
	
	@Autowired
	protected BookingDAO bookingDAO;
	
	@Autowired
	protected BookingVsRoomConverter bookingVsRoomConverter;
	
	@Autowired
	protected SacService sacService;
	
	@Autowired
	protected BookingVsRoomDAO bookingVsRoomDAO;
	
	@Autowired
	protected BookingInfoConverter bookingInfoConverter;
	
	@Autowired
	protected BookingInfoDAO bookingInfoDAO;
	
	@Value("${generic.error.code}")
	protected String genericErrorCode;
	
	@Value("${generic.error.message}")
	protected String genericErrorMessage;
	
	@Autowired
	protected CashFreeApi cashFreeApi;
	
	@Autowired
	protected GatewayService gatewayService;
}
