/*package com.orastays.bookingserver.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.orastays.booking.bookingserver.model.cashfree.CancellationSlabModel;
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
import com.orastays.bookingserver.entity.BookingVsRoomEntity;
import com.orastays.bookingserver.entity.CancellationEntity;
import com.orastays.bookingserver.entity.CancellationVsRoomEntity;
import com.orastays.bookingserver.exceptions.FormExceptions;
import com.orastays.bookingserver.helper.CashFreeApi;
import com.orastays.bookingserver.helper.Util;
import com.orastays.bookingserver.model.RefundModel;

@Component
public class CancellationHelper {

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected CashFreeApi cashFreeApi;

	@Autowired
	protected CancellationDAO cancellationDAO;

	@Autowired
	protected CancellationVsRoomDAO cancellationVsRoomDAO;

	@Autowired
	protected BookingVsRoomDAO bookingVsRoomDAO;

	@Autowired
	protected BookingDAO bookingDAO;

	@Autowired
	protected BookingVsPaymentDAO bookingVsPaymentDAO;

	public void performCancellation(BookingEntity bookingEntity) throws FormExceptions {
		Map<Long, CancellationSlabModel> roomCancellationMap = new HashMap<>();
		bookingEntity.getBookingVsRoomEntities().parallelStream().forEach(room -> {
			// make a rest call to property server to fetch cancellation slab id and
			// percentage
			// by providing room id and number of days
			// for now assume a cancellation slab id to be 1 and percentage to be 50

			// calculate pending days
			int numberOfDaysLeft = Util.getDayDiff(bookingEntity.getCheckoutDate(),
					Util.formatDateFromEntity(new Date()));

			// make the rest call to property server to fetch details

			CancellationSlabModel cancellationSlabModel = new CancellationSlabModel();
			cancellationSlabModel.setCancellationSlabId("1");
			cancellationSlabModel.setPercentage("50");

			// update map
			roomCancellationMap.put(room.getBookingVsRoomId(), cancellationSlabModel);

		});

		double refundAmount = 0.0;
		for (BookingVsRoomEntity bookingVsRoomEntity : bookingEntity.getBookingVsRoomEntities()) {
			double roomActualPrice = Double.parseDouble(bookingVsRoomEntity.getRoomActualPrice());
			double roomRefundAmount = roomActualPrice * 0.01 * Double
					.parseDouble(roomCancellationMap.get(bookingVsRoomEntity.getBookingVsRoomId()).getPercentage()) * Integer.parseInt(bookingEntity.getNumOfDays());
			refundAmount += roomRefundAmount;
		}

		// perform synchronization
		// check db again before refunding

		// get the cashless booking vs payment entity from list of
		// bookingvspaymententities
		// to achieve this consider the entity which has a referenceId
		// only cashless transactions will have a reference id

		BookingVsPaymentEntity bookingVsPaymentEntity = new BookingVsPaymentEntity();
		for (BookingVsPaymentEntity bookingVsPaymentEntity2 : bookingEntity.getBookingVsPaymentEntities()) {
			bookingVsPaymentEntity = bookingVsPaymentEntity2;
		}


		RefundModel refundModel = cashFreeApi.initiateRefund(bookingEntity, bookingVsPaymentEntity,
				Util.roundTo2Places(refundAmount), AuthConstant.USER_CANCELLED_BOOKING);
		if (refundModel.getStatus().equalsIgnoreCase(AuthConstant.CASHFREE_OK)) {
			
			
			// set booking vs payment entity
			bookingVsPaymentEntity.setModifiedBy(bookingVsPaymentEntity.getCreatedBy());
			bookingVsPaymentEntity.setModifiedDate(Util.getCurrentDateTime());
			bookingVsPaymentEntity.setStatus(PaymentStatus.CANCELLED.ordinal());
			
			CancellationEntity cancellationEntity = new CancellationEntity();
			cancellationEntity.setCreatedBy(bookingEntity.getCreatedBy());
			cancellationEntity.setCreatedDate(Util.getCurrentDateTime());
			cancellationEntity.setStatus(Status.INACTIVE.ordinal());
			cancellationEntity.setReasonForCancellation(AuthConstant.USER_CANCELLED_BOOKING);
			cancellationEntity.setTotalAmountPaid(bookingVsPaymentEntity.getOrderAmount());
			cancellationEntity.setTotalAmountRefunded(Util.roundTo2Places(refundAmount));
			//cancellationEntity.setTotalPaybleWithoutGst(bookingEntity.getTotalPaybleWithoutGST());
			cancellationEntity.setBookingEntity(bookingEntity);
			cancellationEntity.setUserId(String.valueOf(bookingVsPaymentEntity.getCreatedBy()));

			Long id = (Long) cancellationDAO.save(cancellationEntity);
			CancellationEntity cancellationEntity2 = cancellationDAO.find(id);

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

			// update booking entity
			bookingEntity.setModifiedBy(bookingEntity.getCreatedBy());
			bookingEntity.setModifiedDate(Util.getCurrentDateTime());
			bookingEntity.setStatus(BookingStatus.CANCELLED.ordinal());
			bookingDAO.update(bookingEntity);

			// update booking vs payment entity
			bookingVsPaymentDAO.update(bookingVsPaymentEntity);

			for (BookingVsPaymentEntity bpe : bookingEntity.getBookingVsPaymentEntities()) {
				if (bpe.getGatewayEntity().getGatewayId() != bookingVsPaymentEntity.getGatewayEntity().getGatewayId()) {
					// cash payment checking

					bpe.setModifiedBy(bpe.getCreatedBy());
					bpe.setModifiedDate(Util.getCurrentDateTime());
					bpe.setStatus(PaymentStatus.CANCELLED.ordinal());
					bookingVsPaymentDAO.update(bpe);
				}
			}

		}

	}
}
*/