package com.orastays.bookingserver.service;

import com.orastays.bookingserver.entity.BookingVsPaymentEntity;

public interface BookingVsPaymentService {
	BookingVsPaymentEntity getBookingVsPaymentEntityByOrderId(String orderId);
}
