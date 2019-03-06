package com.orastays.bookingserver.serviceImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.orastays.bookingserver.constants.BookingConstants;
import com.orastays.bookingserver.constants.BookingStatus;
import com.orastays.bookingserver.entity.BookingEntity;
import com.orastays.bookingserver.exceptions.FormExceptions;
import com.orastays.bookingserver.helper.Util;
import com.orastays.bookingserver.model.BookingModel;
import com.orastays.bookingserver.model.PaymentModel;
import com.orastays.bookingserver.service.BookingService;

@Service
@Transactional
public class BookingServiceImpl extends BaseServiceImpl implements BookingService {
	private static final Logger logger = LogManager.getLogger(BookingServiceImpl.class);

	@Override
	public PaymentModel addBooking(BookingModel bookingModel) throws FormExceptions {
		logger.info("addBooking -- START");

		BookingEntity bookingEntity = bookingUtil.generateBookingEntity(bookingModel);

		PaymentModel paymentModel = null;
		// check payment mode
		if (bookingModel.getFormOfPayment().getMode().equalsIgnoreCase(BookingConstants.MODE_CASHLESS)) {
			// proceed with online payment
			paymentModel = bookingUtil.bookRoomForCashLessPayments(bookingModel, bookingEntity);
		}

		logger.info("addBooking -- END");
		return paymentModel;
	}

	@Override
	public BookingModel validateBooking(BookingModel bookingModel) throws FormExceptions {
		logger.info("validateBooking -- START");

		BookingModel bookingModel2 = null;
		try {
			Map<String, String> innerMap1 = new LinkedHashMap<>();
			innerMap1.put("status", String.valueOf(BookingStatus.BOOKED.ordinal()));
			innerMap1.put("bookingId", bookingModel.getBookingId());
			innerMap1.put("propertyId", bookingModel.getPropertyId());
			innerMap1.put("userId", bookingModel.getUserId());
			Map<String, Map<String, String>> outerMap1 = new LinkedHashMap<>();
			outerMap1.put("eq", innerMap1);

			Map<String, Map<String, Map<String, String>>> alliasMap = new LinkedHashMap<>();
			alliasMap.put(entitymanagerPackagesToScan + ".BookingEntity", outerMap1);

			bookingModel2 = bookingConverter.entityToModel(bookingDAO.fetchObjectBySubCiteria(alliasMap));
		} catch (Exception e) {
			logger.info("Exception in validateBooking -- " + Util.errorToString(e));
		}

		logger.info("validateBooking -- END");

		return bookingModel2;
	}

	@Override
	public List<BookingModel> getPropertyBookings(BookingModel bookingModel) throws FormExceptions {
		logger.info("getPropertyBookings -- START");

		List<BookingModel> bookingModels = null;
/*		try {
			Map<String, String> innerMap1 = new LinkedHashMap<>();
			innerMap1.put("status", String.valueOf(BookingStatus.BOOKED.ordinal()));
			innerMap1.put("propertyId", bookingModel.getPropertyId());
			Map<String, Map<String, String>> outerMap1 = new LinkedHashMap<>();
			outerMap1.put("eq", innerMap1);

			Map<String, Map<String, Map<String, String>>> alliasMap = new LinkedHashMap<>();
			alliasMap.put(entitymanagerPackagesToScan + ".BookingEntity", outerMap1);

			bookingModels = bookingConverter.entityListToModelList(bookingDAO.fetchListBySubCiteria(alliasMap));
		} catch (Exception e) {
			logger.info("Exception in getPropertyBookings -- " + Util.errorToString(e));
		}*/
		try {
			bookingModels = bookingConverter.entityListToModelList(bookingDAO.getPropertyBookingsByDate(bookingModel.getCheckinDate(), bookingModel.getCheckoutDate(), bookingModel.getPropertyId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("getPropertyBookings -- END");

		return bookingModels;
	}

	@Override
	public List<BookingModel> getUserBookings(BookingModel bookingModel) throws FormExceptions {
		logger.info("getUserBookings -- START");

		List<BookingModel> bookingModels = null;
		/*try {
			Map<String, String> innerMap1 = new LinkedHashMap<>();
			innerMap1.put("status", String.valueOf(BookingStatus.BOOKED.ordinal()));
			innerMap1.put("userId", bookingModel.getUserId());
			Map<String, Map<String, String>> outerMap1 = new LinkedHashMap<>();
			outerMap1.put("eq", innerMap1);

			Map<String, Map<String, Map<String, String>>> alliasMap = new LinkedHashMap<>();
			alliasMap.put(entitymanagerPackagesToScan + ".BookingEntity", outerMap1);

			bookingModels = bookingConverter.entityListToModelList(bookingDAO.fetchListBySubCiteria(alliasMap));
		} catch (Exception e) {
			logger.info("Exception in getUserBookings -- " + Util.errorToString(e));
		}*/
		try {
			bookingModels = bookingConverter.entityListToModelList(bookingDAO.getUserBookingsByDate(bookingModel.getCheckinDate(), bookingModel.getCheckoutDate(), bookingModel.getUserId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("getUserBookings -- END");

		return bookingModels;
	}

	@Override
	public List<BookingModel> getBookings(BookingModel bookingModel) throws FormExceptions {
		logger.info("getBookings -- START");

		List<BookingModel> bookingModels = null;
		try {
			bookingModels = bookingConverter.entityListToModelList(bookingDAO.getBookingsByCheckInDate(
					bookingModel.getCheckinDate(), bookingModel.getPropertyId(), bookingModel.getAccomodationType()));
		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isInfoEnabled()) {
				logger.info("Exception in getBookings -- " + Util.errorToString(e));
			}
		}

		logger.info("getBookings -- END");

		return bookingModels;
	}

}
