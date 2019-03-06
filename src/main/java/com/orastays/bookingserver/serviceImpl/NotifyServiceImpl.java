package com.orastays.bookingserver.serviceImpl;

import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orastays.bookingserver.constants.BookingConstants;
import com.orastays.bookingserver.constants.BookingStatus;
import com.orastays.bookingserver.constants.PaymentStatus;
import com.orastays.bookingserver.constants.RoomStatus;
import com.orastays.bookingserver.constants.Status;
import com.orastays.bookingserver.dao.BookingDAO;
import com.orastays.bookingserver.dao.BookingVsPaymentDAO;
import com.orastays.bookingserver.dao.BookingVsRoomDAO;
import com.orastays.bookingserver.dao.CancellationDAO;
import com.orastays.bookingserver.dao.CancellationVsRoomDAO;
import com.orastays.bookingserver.entity.BookingEntity;
import com.orastays.bookingserver.entity.BookingVsPaymentEntity;
import com.orastays.bookingserver.entity.CancellationEntity;
import com.orastays.bookingserver.entity.CancellationVsRoomEntity;
import com.orastays.bookingserver.helper.RoomUpdateAfterGatewayPayment;
import com.orastays.bookingserver.helper.Util;
import com.orastays.bookingserver.service.BookingVsPaymentService;
import com.orastays.bookingserver.service.NotifyService;

@Transactional
@Service
public class NotifyServiceImpl implements NotifyService {
	private static final Logger logger = LogManager.getLogger(NotifyServiceImpl.class);

	@Autowired
	protected BookingDAO bookingDAO;

	@Autowired
	protected BookingVsRoomDAO bookingVsRoomDAO;

	@Autowired
	protected BookingVsPaymentService bookingVsPaymentService;

	@Autowired
	protected BookingVsPaymentDAO bookingVsPaymentDAO;

	@Autowired
	protected CancellationDAO cancellationDAO;

	@Autowired
	protected CancellationVsRoomDAO cancellationVsRoomDAO;

	@Autowired
	protected RoomUpdateAfterGatewayPayment roomUpdateAfterGatewayPayment;

	@Override
	public String updateBookingStatus(Map<String, String> paramMap) {
		logger.info("updateBookingStatus -- START");
		String url = "";
		if (paramMap.get(BookingConstants.txStatus).equalsIgnoreCase(BookingConstants.SUCCESS)) {
			// validate booking and initiate refund if already room booked
			url = roomUpdateAfterGatewayPayment.checkRoomStatusAndBookOrRefund(paramMap);

		} else if (paramMap.get(BookingConstants.txStatus).equalsIgnoreCase(BookingConstants.FLAGGED)) {

		} else if (paramMap.get(BookingConstants.txStatus).equalsIgnoreCase(BookingConstants.PENDING)) {

		} else if (paramMap.get(BookingConstants.txStatus).equalsIgnoreCase(BookingConstants.FAILED)) { // successful but kept on hold by risk
																				// system
			url = updateBookingOnFaliureOrCancelled(paramMap);
		} else if (paramMap.get(BookingConstants.txStatus).equalsIgnoreCase(BookingConstants.CANCELLED)) {
			url = updateBookingOnFaliureOrCancelled(paramMap);
		}

		logger.info("updateBookingStatus -- END");
		return url;
	}

	public String updateBookingOnFaliureOrCancelled(Map<String, String> paramMap) {
		logger.info("updateBookingOnFaliureOrCancelled -- START");
		String returnUrl = null;
		BookingVsPaymentEntity bookingVsPaymentEntity = bookingVsPaymentService
				.getBookingVsPaymentEntityByOrderId(paramMap.get(BookingConstants.orderId));

		bookingVsPaymentEntity.setTxStatus(paramMap.get(BookingConstants.txStatus));
		bookingVsPaymentEntity.setReferenceId(paramMap.get(BookingConstants.referenceId));
		bookingVsPaymentEntity.setPaymentMode(paramMap.get(BookingConstants.paymentMode));
		bookingVsPaymentEntity.setTxTime(paramMap.get(BookingConstants.txTime));
		bookingVsPaymentEntity.setTxMsg(paramMap.get(BookingConstants.txMsg));
		// while fetching check cancelled status and txStatus for failed and cancelled
		bookingVsPaymentEntity.setStatus(PaymentStatus.CANCELLED.ordinal());
		bookingVsPaymentEntity.setModifiedBy(bookingVsPaymentEntity.getCreatedBy());
		bookingVsPaymentEntity.setModifiedDate(Util.getCurrentDateTime());
		try {
			// update booking vs payment

			bookingVsPaymentDAO.update(bookingVsPaymentEntity);

			// update booking

			BookingEntity bookingEntity = bookingDAO.find(bookingVsPaymentEntity.getBookingEntity().getBookingId());

			returnUrl = bookingEntity.getFailureURL();
			
			bookingEntity.setModifiedBy(bookingEntity.getCreatedBy());
			bookingEntity.setModifiedDate(Util.getCurrentDateTime());
			bookingEntity.setStatus(BookingStatus.CANCELLED.ordinal());

			bookingDAO.update(bookingEntity);

			// insert into cancellation
			CancellationEntity cancellationEntity = new CancellationEntity();
			cancellationEntity.setCreatedBy(bookingEntity.getCreatedBy());
			cancellationEntity.setCreatedDate(Util.getCurrentDateTime());
			cancellationEntity.setStatus(Status.INACTIVE.ordinal());
			cancellationEntity.setReasonForCancellation(BookingConstants.USER_CANCELLED_BOOKING_IN_GATEWAY);
			cancellationEntity.setTotalAmountPaid(bookingVsPaymentEntity.getOrderAmount());
			cancellationEntity.setTotalAmountRefunded(bookingVsPaymentEntity.getAmountPaid());
			// cancellationEntity.setTotalPaybleWithoutGst(bookingEntity.getTotalPaybleWithoutGST());
			cancellationEntity.setBookingEntity(bookingEntity);
			cancellationEntity.setUserId(String.valueOf(bookingVsPaymentEntity.getCreatedBy()));

			Long id = (Long) cancellationDAO.save(cancellationEntity);
			CancellationEntity cancellationEntity2 = cancellationDAO.find(id);

			// update booking vs room

			bookingEntity.getBookingVsRoomEntities().parallelStream().forEach(room -> {
				room.setModifiedBy(room.getCreatedBy());
				room.setModifiedDate(Util.getCurrentDateTime());
				room.setStatus(RoomStatus.INACTIVE.ordinal());
				bookingVsRoomDAO.update(room);

				// insert into cancellation vs room
				CancellationVsRoomEntity cancellationVsRoomEntity = new CancellationVsRoomEntity();
				cancellationVsRoomEntity.setCreatedBy(bookingEntity.getCreatedBy());
				cancellationVsRoomEntity.setCreatedDate(Util.getCurrentDateTime());
				cancellationVsRoomEntity.setStatus(Status.INACTIVE.ordinal());
				cancellationVsRoomEntity.setBookingVsRoomEntity(room);
				cancellationVsRoomEntity.setCancellationEntity(cancellationEntity2);

				cancellationVsRoomDAO.save(cancellationVsRoomEntity);
			});

			// for partial payment the cash payment status also needs to be set to cancelled
			bookingEntity.getBookingVsPaymentEntities().forEach(bpe -> {
				// there can be max 2 rows for a payment. One for cash, one for cashless
				// if cash then only update else update will be done in the section update
				// booking vs payment

				if (bpe.getGatewayEntity().getGatewayId() != bookingVsPaymentEntity.getGatewayEntity().getGatewayId()) {
					// cash payment checking

					bpe.setModifiedBy(bpe.getCreatedBy());
					bpe.setModifiedDate(Util.getCurrentDateTime());
					bpe.setStatus(PaymentStatus.CANCELLED.ordinal());
					bookingVsPaymentDAO.update(bpe);
				}
				
			});
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("updateBookingOnFaliureOrCancelled -- END");
		return returnUrl;
	}
}
