package com.orastays.bookingserver.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.orastays.bookingserver.converter.BookingConverter;
import com.orastays.bookingserver.dao.BookingDAO;
import com.orastays.bookingserver.utils.BookingUtil;

public class BaseServiceImpl {
	@Autowired
	protected BookingUtil bookingUtil;

	@Value("${entitymanager.packagesToScan}")
	protected String entitymanagerPackagesToScan;

	@Autowired
	protected BookingDAO bookingDAO;

	@Autowired
	protected BookingConverter bookingConverter;
}
