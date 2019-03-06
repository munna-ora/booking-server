package com.orastays.bookingserver.service;

import java.util.List;

import com.orastays.bookingserver.exceptions.FormExceptions;
import com.orastays.bookingserver.model.BookingModel;

public interface CancellationService {
	BookingModel cancelBooking(BookingModel bookingModel) throws FormExceptions;

	List<BookingModel> getUserCancellations(BookingModel bookingModel) throws FormExceptions;

	List<BookingModel> getPropertyCancellations(BookingModel bookingModel) throws FormExceptions;
}
