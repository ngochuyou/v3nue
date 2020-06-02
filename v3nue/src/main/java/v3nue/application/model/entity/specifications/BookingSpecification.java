/**
 * 
 */
package v3nue.application.model.entity.specifications;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Booking;
import v3nue.application.model.entities.EventType;
import v3nue.application.model.entities.Venue;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecificationWithDAO;
import v3nue.core.service.ServiceResult;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { Booking.class })
public class BookingSpecification extends CompositeSpecificationWithDAO<Booking> {

	@Override
	public ServiceResult<Booking> isSatisfiedBy(Booking entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<>();
		int status = OK;
		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		Venue venue = entity.getVenue();

		if (venue == null || dao.findById(venue.getId(), Venue.class) == null) {
			messages.put("venue", "Event venue must not be empty and must be a valid Venue.");
			status = BAD;
		}

		if (entity.getType() == null || dao.findById(entity.getType().getId(), EventType.class) == null) {
			messages.put("type", "Event type must not be empty and must be a valid Event Type.");
			status = BAD;
		}

		if (entity.getEventDate() == null) {
			messages.put("eventDate", "Event date must not be empty");
			status = BAD;
		}

		Date expiryDate = entity.getExpiryDate();

		if (expiryDate == null || expiryDate.before(entity.getCreatedDate())) {
			messages.put("expiryDate", "Expiry date must not be empty and must be after booking date.");
			status = BAD;
		}

		Date startTime = entity.getStartTime();
		Date endTime = entity.getEndTime();

		if (startTime == null || endTime == null || startTime.after(endTime) || venue == null || expiryDate == null) {
			messages.put("startTime", "Event start time and end time must not be empty and must be properly picked.");
			messages.put("endTime", "Event start time and end time must not be empty and must be properly picked.");
			status = BAD;
		} else {
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			Root<Booking> root = query.from(Booking.class);
			// @formatter:off
			query.select(builder.count(root)).where(builder
					.and(builder.equal(root.get("startTime"), startTime),
							builder.equal(root.get("endTime"), endTime),
							builder.equal(root.get("venue").get("id"), venue.getId()),
							builder.or(builder.equal(root.get("isActive"), true),
									builder.greaterThan(root.get("expiryDate"), new Date()))));
			// @formatter:on
			if (dao.count(query) != 0) {
				messages.put("startTime",
						"Can not have more than one active Booking with the same start time and end time at the same Venue.");
				messages.put("endTime",
						"Can not have more than one active Booking with the same start time and end time at the same Venue.");
				status = CONFLICT;
			}
		}

		return new ServiceResult<Booking>(entity, messages, status);
	}

}
