package com.orastays.bookingserver.service;

import java.util.List;

import com.orastays.bookingserver.exceptions.FormExceptions;
import com.orastays.bookingserver.model.BookingModel;
import com.orastays.bookingserver.model.PaymentModel;

public interface BookingService {
	PaymentModel addBooking(BookingModel bookingModel) throws FormExceptions;
	BookingModel validateBooking(BookingModel bookingModel) throws FormExceptions;
	List<BookingModel> getPropertyBookings(BookingModel bookingModel) throws FormExceptions;
	List<BookingModel> getUserBookings(BookingModel bookingModel) throws FormExceptions;
	List<BookingModel> getBookings(BookingModel bookingModel) throws FormExceptions;
}
