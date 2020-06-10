/**
 * 
 */
package v3nue.application.controllers;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import v3nue.application.model.entities.Account;
import v3nue.application.model.entities.Booking;
import v3nue.application.service.services.BookingService;
import v3nue.core.service.ServiceResult;

/**
 * @author Ngoc Huy
 *
 */
@RestController
@RequestMapping("/api/booking")
public class BookingController extends BaseController {

	@Autowired
	private BookingService service;

	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER)
	@GetMapping("/paginate")
	public ResponseEntity<?> paginating() {
		super.checkAccountScopes(READ);
		super.openSession();

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Booking> root = query.from(Booking.class);
		// @formatter:off
		query.select(builder.count(root))
			.where(builder.equal(root.get("isActive"), true));
		// @formatter:on
		return handle(new PaginatingSet(dao.count(query), 10), 200, false);
	}

	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER)
	@GetMapping
	public ResponseEntity<?> getBookingList(@RequestParam(name = "p", required = false, defaultValue = "0") int page) {
		super.openSession();
		super.checkAccountScopes(READ);

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Booking> query = builder.createQuery(Booking.class);

		query.from(Booking.class);

		return handle(dao.find(query, calculateFirstIndex(page, 10), 10), 200, false);
	}

	@PostMapping
	public ResponseEntity<?> createBooking(Authentication authentication, @RequestBody(required = true) Booking model) {
		super.openSession();

		if (authentication != null) {
			model.setCustomer(dao.findById(authentication.getName(), Account.class));
		}

		Date date = new Date();
		Calendar c = Calendar.getInstance();

		c.setTime(date);
		c.add(Calendar.DATE, 15);
		date = c.getTime();
		model.setExpiryDate(date);

		ServiceResult<Booking> result = dao.insert(service.doMandatory(model), Booking.class);

		if (result.isOkay()) {
			return handleSuccess(result.getEntity());
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}
}
