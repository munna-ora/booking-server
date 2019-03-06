package com.orastays.bookingserver.controller;

import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orastays.bookingserver.constants.BookingConstants;
import com.orastays.bookingserver.exceptions.FormExceptions;
import com.orastays.bookingserver.helper.Util;
import com.orastays.bookingserver.model.BookingModel;
import com.orastays.bookingserver.model.PaymentModel;
import com.orastays.bookingserver.model.ResponseModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Api(value = "Booking", tags = "Booking")
public class BookingController extends BookingBaseController {
	private static final Logger logger = LogManager.getLogger(BookingController.class);

	@PostMapping(value = "/add-booking", produces = "application/json")
	@ApiOperation(value = "Add Booking", response = ResponseModel.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 201, message = "Please Try after Sometime!!!") })

	public ResponseEntity<ResponseModel> addBooking(@RequestBody BookingModel bookingModel) {

		logger.info("addBooking -- START");

		ResponseModel responseModel = new ResponseModel();
		Util.printLog(bookingModel, BookingConstants.INCOMING, "add-booking", request);
		try {
			PaymentModel paymentModel = bookingService.addBooking(bookingModel);
			responseModel.setResponseBody(paymentModel);
			responseModel.setResponseCode(commonSuccessCode);
			responseModel.setResponseMessage(commonSuccessMessage);
		} catch (FormExceptions fe) {
			for (Entry<String, Exception> entry : fe.getExceptions().entrySet()) {
				responseModel.setResponseCode(entry.getKey());
				responseModel.setResponseMessage(entry.getValue().getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("FormExceptions in add-booking -- {}", Util.errorToString(fe));
				}
				break;
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception in Add Booking -- {}", Util.errorToString(e));
			}
			responseModel.setResponseCode(commonErrorCode);
			responseModel.setResponseMessage(commonErrorMessage);
		}

		Util.printLog(responseModel, BookingConstants.OUTGOING, "add-booking", request);

		logger.info("addBooking -- END");

		if (responseModel.getResponseCode().equals(commonSuccessCode)) {
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/validate-booking", produces = "application/json")
	@ApiOperation(value = "Validate Booking", response = ResponseModel.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 201, message = "Please Try after Sometime!!!") })

	public ResponseEntity<ResponseModel> validateBooking(@RequestBody BookingModel bookingModel) {
		if (logger.isInfoEnabled()) {
			logger.info("validateBooking -- START");
		}

		ResponseModel responseModel = new ResponseModel();
		Util.printLog(bookingModel, BookingConstants.INCOMING, "validate-booking", request);
		try {
			BookingModel bookingModel2 = bookingService.validateBooking(bookingModel);
			responseModel.setResponseBody(bookingModel2);
			responseModel.setResponseCode(commonSuccessCode);
			responseModel.setResponseMessage(commonSuccessMessage);
		} catch (FormExceptions fe) {
			for (Entry<String, Exception> entry : fe.getExceptions().entrySet()) {
				responseModel.setResponseCode(entry.getKey());
				responseModel.setResponseMessage(entry.getValue().getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("FormExceptions in Validate Booking -- " + Util.errorToString(fe));
				}
				break;
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception in Validate Booking -- " + Util.errorToString(e));
			}
			responseModel.setResponseCode(commonErrorCode);
			responseModel.setResponseMessage(commonErrorMessage);
		}

		Util.printLog(responseModel, BookingConstants.OUTGOING, "validate-booking", request);

		if (logger.isInfoEnabled()) {
			logger.info("validateBooking -- END");
		}

		if (responseModel.getResponseCode().equals(commonSuccessCode)) {
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/get-property-bookings", produces = "application/json")
	@ApiOperation(value = "Property Bookings", response = ResponseModel.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 201, message = "Please Try after Sometime!!!") })

	public ResponseEntity<ResponseModel> getPropertyBookings(@RequestBody BookingModel bookingModel) {
		if (logger.isInfoEnabled()) {
			logger.info("getPropertyBookings -- START");
		}

		ResponseModel responseModel = new ResponseModel();
		Util.printLog(bookingModel, BookingConstants.INCOMING, "get-property-bookings", request);
		try {
			List<BookingModel> bookingModels = bookingService.getPropertyBookings(bookingModel);
			responseModel.setResponseBody(bookingModels);
			responseModel.setResponseCode(commonSuccessCode);
			responseModel.setResponseMessage(commonSuccessMessage);
		} catch (FormExceptions fe) {
			for (Entry<String, Exception> entry : fe.getExceptions().entrySet()) {
				responseModel.setResponseCode(entry.getKey());
				responseModel.setResponseMessage(entry.getValue().getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("FormExceptions in getPropertyBookings -- " + Util.errorToString(fe));
				}
				break;
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception in getPropertyBookings -- " + Util.errorToString(e));
			}
			responseModel.setResponseCode(commonErrorCode);
			responseModel.setResponseMessage(commonErrorMessage);
		}

		Util.printLog(responseModel, BookingConstants.OUTGOING, "get-property-bookings", request);

		if (logger.isInfoEnabled()) {
			logger.info("getPropertyBookings -- END");
		}

		if (responseModel.getResponseCode().equals(commonSuccessCode)) {
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/get-user-bookings", produces = "application/json")
	@ApiOperation(value = "User Bookings", response = ResponseModel.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 201, message = "Please Try after Sometime!!!") })

	public ResponseEntity<ResponseModel> getUserBookings(@RequestBody BookingModel bookingModel) {
		logger.info("getUserBookings -- START");

		ResponseModel responseModel = new ResponseModel();
		Util.printLog(bookingModel, BookingConstants.INCOMING, "get-user-bookings", request);
		try {
			List<BookingModel> bookingModels = bookingService.getUserBookings(bookingModel);
			responseModel.setResponseBody(bookingModels);
			responseModel.setResponseCode(commonSuccessCode);
			responseModel.setResponseMessage(commonSuccessMessage);
		} catch (FormExceptions fe) {
			for (Entry<String, Exception> entry : fe.getExceptions().entrySet()) {
				responseModel.setResponseCode(entry.getKey());
				responseModel.setResponseMessage(entry.getValue().getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("FormExceptions in getPropertyBookings -- " + Util.errorToString(fe));
				}
				break;
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception in getUserBookings -- " + Util.errorToString(e));
			}
			responseModel.setResponseCode(commonErrorCode);
			responseModel.setResponseMessage(commonErrorMessage);
		}

		Util.printLog(responseModel, BookingConstants.OUTGOING, "get-user-bookings", request);

		logger.info("getUserBookings -- END");

		if (responseModel.getResponseCode().equals(commonSuccessCode)) {
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<ResponseModel> getBookings(@RequestBody BookingModel bookingModel) {
		if (logger.isInfoEnabled()) {
			logger.info("getBookings -- START");
		}

		ResponseModel responseModel = new ResponseModel();
		Util.printLog(bookingModel, BookingConstants.INCOMING, "get-bookings", request);
		try {
			List<BookingModel> bookingModels = bookingService.getBookings(bookingModel);
			responseModel.setResponseBody(bookingModels);
			responseModel.setResponseCode(commonSuccessCode);
			responseModel.setResponseMessage(commonSuccessMessage);
		} catch (FormExceptions fe) {
			for (Entry<String, Exception> entry : fe.getExceptions().entrySet()) {
				responseModel.setResponseCode(entry.getKey());
				responseModel.setResponseMessage(entry.getValue().getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("FormExceptions in Get Bookings -- " + Util.errorToString(fe));
				}
				break;
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception in Get Bookings -- " + Util.errorToString(e));
			}
			responseModel.setResponseCode(commonErrorCode);
			responseModel.setResponseMessage(commonErrorMessage);
		}

		Util.printLog(responseModel, BookingConstants.OUTGOING, "get-bookings", request);

		logger.info("getBookings -- END");

		if (responseModel.getResponseCode().equals(commonSuccessCode)) {
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
		}
	}

}
