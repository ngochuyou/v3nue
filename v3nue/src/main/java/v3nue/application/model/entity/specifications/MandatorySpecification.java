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

import v3nue.application.model.entities.Mandatory;
import v3nue.application.model.entities.MandatoryType;
import v3nue.core.dao.DatabaseOperationResult;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecificationWithDAO;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { Mandatory.class })
public class MandatorySpecification extends CompositeSpecificationWithDAO<Mandatory> {

	@Override
	public DatabaseOperationResult<Mandatory> isSatisfiedBy(Mandatory entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<>();
		int status = OK;

		if (entity.getPrice() < 0) {
			messages.put("price", "price can not be negative.");
			status = BAD;
		}

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		MandatoryType type = entity.getType();

		if (type == null) {
			messages.put("type", "Mandatory type can not be empty.");
			status = BAD;
		} else {
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			Root<MandatoryType> root = query.from(MandatoryType.class);

			query.select(builder.count(root));
			query.where(builder.equal(root.get("id"), type.getId()));

			if (dao.count(query) == 0) {
				messages.put("type", "Annonymous Manadatory type.");
				status = CONFLICT;
			}
		}

		if (entity.getSuppliers() == null || entity.getSuppliers().size() == 0) {
			messages.put("suppliers", "Suppliers can not be empty and must all be valid.");
			status = BAD;
		}

		return new DatabaseOperationResult<Mandatory>(entity, messages, status);
	}

}
