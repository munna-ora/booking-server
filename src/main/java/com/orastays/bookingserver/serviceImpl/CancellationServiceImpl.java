/*package com.orastays.bookingserver.serviceImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.orastays.bookingserver.constants.BookingStatus;
import com.orastays.bookingserver.converter.BookingConverter;
import com.orastays.bookingserver.dao.BookingDAO;
import com.orastays.bookingserver.exceptions.FormExceptions;
import com.orastays.bookingserver.helper.Util;
import com.orastays.bookingserver.model.BookingModel;
import com.orastays.bookingserver.service.CancellationService;

@Service
@Transactional
public class CancellationServiceImpl implements CancellationService{

	private static final Logger logger = LogManager.getLogger(CancellationService.class);
	
	@Value("${entitymanager.packagesToScan}")
	protected String entitymanagerPackagesToScan;
	
	@Autowired
	protected CancellationValidation cancellationValidation;
	
	@Autowired
	protected BookingDAO bookingDAO;

	@Autowired
	protected BookingConverter bookingConverter;
	
	@Override
	public BookingModel cancelBooking(BookingModel bookingModel) throws FormExceptions {
		
		if (logger.isInfoEnabled()) {
			logger.info("cancelBooking -- START");
		}
		//cancellationValidation.validateCancelBooking(bookingModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("cancelBooking -- END");
		}
		
	}

	@Override
	public List<BookingModel> getUserCancellations(BookingModel bookingModel) throws FormExceptions {
		// TODO Auto-generated method stub
		if (logger.isInfoEnabled()) {
			logger.info("getUserCancellations -- START");
		}
		
		List<BookingModel> bookingModels = null;
		try {
			Map<String, String> innerMap1 = new LinkedHashMap<>();
			innerMap1.put("status", String.valueOf(BookingStatus.CANCELLED.ordinal()));
			innerMap1.put("userId", bookingModel.getUserId());
			Map<String, Map<String, String>> outerMap1 = new LinkedHashMap<>();
			outerMap1.put("eq", innerMap1);

			Map<String, Map<String, Map<String, String>>> alliasMap = new LinkedHashMap<>();
			alliasMap.put(entitymanagerPackagesToScan + ".BookingEntity", outerMap1);

			bookingModels = bookingConverter.entityListToModelList(bookingDAO.fetchListBySubCiteria(alliasMap));
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception in getUserCancellations -- " + Util.errorToString(e));
			}
		}

		
		if (logger.isInfoEnabled()) {
			logger.info("getUserCancellations -- END");
		}
		return bookingModels;
	}

	@Override
	public List<BookingModel> getPropertyCancellations(BookingModel bookingModel) throws FormExceptions {
		// TODO Auto-generated method stub
		if (logger.isInfoEnabled()) {
			logger.info("getPropertyCancellations -- START");
		}
		List<BookingModel> bookingModels = null;
		try {
			Map<String, String> innerMap1 = new LinkedHashMap<>();
			innerMap1.put("status", String.valueOf(BookingStatus.CANCELLED.ordinal()));
			innerMap1.put("propertyId", bookingModel.getPropertyId());
			Map<String, Map<String, String>> outerMap1 = new LinkedHashMap<>();
			outerMap1.put("eq", innerMap1);

			Map<String, Map<String, Map<String, String>>> alliasMap = new LinkedHashMap<>();
			alliasMap.put(entitymanagerPackagesToScan + ".BookingEntity", outerMap1);

			bookingModels = bookingConverter.entityListToModelList(bookingDAO.fetchListBySubCiteria(alliasMap));
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception in getPropertyCancellations -- " + Util.errorToString(e));
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("getPropertyCancellations -- END");
		}
		return bookingModels;
	}


}
*/