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
import v3nue.core.dao.DatabaseOperationResult;
import v3nue.core.model.AbstractFactor;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecificationWithDAO;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { AbstractFactor.class })
public class AbstractFactorSpecification extends CompositeSpecificationWithDAO<AbstractFactor> {

	@Override
	public DatabaseOperationResult<AbstractFactor> isSatisfiedBy(AbstractFactor entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<String, String>();
		int status = OK;
		String id = entity.getId();

		if (id == null || id.length() < 8 || id.length() > 255) {
			messages.put("id", "id length must be between 8 and 255.");
			status = BAD;
		}

		String name = entity.getName();

		if (name == null || name.length() < 1 || name.length() > 255) {
			messages.put("name", "name length must be between 1 and 255.");
			status = BAD;
		}

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<? extends AbstractFactor> root = query.from(entity.getClass());

		// @formatter:off
		query.select(builder.count(root));
		query.where(builder
				.and(builder.notEqual(root.get("id"), entity.getId()), 
						builder.equal(root.get("name"), entity.getName())));
		// @formatter:on
		if (dao.count(query) != 0) {
			messages.put("name", "name must be unique.");
			status = CONFLICT;
		}

		String createdBy = entity.getCreatedBy();

		if (createdBy != null) {
			CriteriaQuery<Long> creatorQuery = builder.createQuery(Long.class);
			Root<Account> creatorRoot = creatorQuery.from(Account.class);

			creatorQuery.select(builder.count(creatorRoot));
			creatorQuery.where(builder.equal(creatorRoot.get("id"), createdBy));

			if (dao.count(creatorQuery) == 0) {
				messages.put("createdBy", "Annonymous creator is prohibited.");
				status = CONFLICT;
			}

			return new DatabaseOperationResult<AbstractFactor>(entity, messages, status);
		}

		messages.put("createdBy", "Creator's id must be a valid information.");

		return new DatabaseOperationResult<AbstractFactor>(entity, messages, BAD);
	}

}
