/**
 * 
 */
package v3nue.application.model.entity.specifications;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecification;
import v3nue.core.model.entity.specification.EntityValidationResult;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { AbstractEntity.class })
public class AbstractEntitySpecification extends CompositeSpecification<AbstractEntity> {

	@Override
	public EntityValidationResult<AbstractEntity> isSatisfiedBy(AbstractEntity entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<String, String>();
		int status = OK;

		if (entity.getId() == null) {
			messages.put("id", "id can not be null.");
			status = BAD;
		}

		if (entity.getCreatedDate() == null || entity.getCreatedDate().after(new Date())) {
			messages.put("createdDate", "Invalid creation date.");
			status = BAD;
		}

		if (entity.getUpdatedDate() == null) {
			messages.put("updatedDate", "Invalid update date.");
			status = BAD;
		}

		return new EntityValidationResult<AbstractEntity>(entity, messages, status);
	}

}
