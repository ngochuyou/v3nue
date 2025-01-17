/**
 * 
 */
package v3nue.application.model.entity.specifications;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Account;
import v3nue.application.model.entities.Booking;
import v3nue.application.model.entities.Contract;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecificationWithDAO;
import v3nue.core.service.ServiceResult;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.StringUtil;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { Contract.class })
public class ContractSpecification extends CompositeSpecificationWithDAO<Contract> {

	@Override
	public ServiceResult<Contract> isSatisfiedBy(Contract entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<>();
		int status = OK;

		if (entity.getAgreedAmount() < 0) {
			messages.put("agreedAmount", "Agreed amount can not be negative.");
			status = BAD;
		}

		if (entity.getTotalAmount() < 0) {
			messages.put("totalAmount", "Total amount can not be negative.");
			status = BAD;
		}

		if (entity.getDepositAmount() < 0) {
			messages.put("depositAmount", "Deposit amount can not be negative and can not exceed total amount.");
			status = BAD;
		}

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		Account supervisor = entity.getSupervisor();

		if (supervisor == null
				|| (supervisor.getRole() != AccountRole.Admin && supervisor.getRole() != AccountRole.Manager)) {
			messages.put("supervisor", "Supervisor can not be empty.");
			status = BAD;
		} else {
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			Root<Account> root = query.from(Account.class);

			query.select(builder.count(root)).where(builder.equal(root.get("id"), supervisor.getId()));

			if (dao.count(query) == 0) {
				messages.put("supervisor", "Annonymous supervisor.");
				status = CONFLICT;
			}
		}

		Booking booking = entity.getBooking();

		if (booking == null) {
			messages.put("booking", "Booking can not be empty.");
			status = BAD;
		} else {
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			Root<Booking> root = query.from(Booking.class);

			query.select(builder.count(root)).where(builder.equal(root.get("id"), booking.getId()));

			if (dao.count(query) == 0) {
				messages.put("booking", "Annonymous booking.");
				status = CONFLICT;
			}
		}

		if (StringUtil.isEmpty(entity.getDescription())) {
			messages.put("description", "Description can not be empty.");
			status = BAD;
		}

		return new ServiceResult<Contract>(entity, messages, status);
	}

}
