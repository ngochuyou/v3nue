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

import v3nue.application.model.entities.FoodsAndDrinks;
import v3nue.application.model.entities.FoodsAndDrinksType;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecificationWithDAO;
import v3nue.core.service.ServiceResult;
import v3nue.core.utils.StringUtil;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { FoodsAndDrinks.class })
public class FoodsAndDrinksSpecification extends CompositeSpecificationWithDAO<FoodsAndDrinks> {

	@Override
	public ServiceResult<FoodsAndDrinks> isSatisfiedBy(FoodsAndDrinks entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<>();
		int status = OK;

		if (entity.getPrice() < 0) {
			messages.put("price", "price can not be negative.");
			status = BAD;
		}

		if (StringUtil.isEmpty(entity.getPhoto())) {
			messages.put("photo", "Foods-and-Drinks must have a photo.");
			status = BAD;
		}

		FoodsAndDrinksType type = entity.getType();

		if (type == null) {
			messages.put("type", "Foods-and-Drinks type can not be empty.");
			status = BAD;
		} else {
			CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			Root<FoodsAndDrinksType> root = query.from(FoodsAndDrinksType.class);

			query.select(builder.count(root)).where(builder.equal(root.get("id"), type.getId()));

			if (dao.count(query) == 0) {
				messages.put("type", "Annonymous Foods-and-Drinks type.");
				status = CONFLICT;
			}
		}

		return new ServiceResult<FoodsAndDrinks>(entity, messages, status);
	}

}
