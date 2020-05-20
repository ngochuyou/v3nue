/**
 * 
 */
package v3nue.application.model.entity.specifications;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Seating;
import v3nue.core.dao.DatabaseOperationResult;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecification;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { Seating.class })
public class SeatingSpecification extends CompositeSpecification<Seating> {

	@Override
	public DatabaseOperationResult<Seating> isSatisfiedBy(Seating entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<>();
		int capacity = entity.getCapacity();

		if (capacity < 0 || capacity > 16777215) {
			messages.put("capacity", "capacity must be between 0 and 16777215.");
			return new DatabaseOperationResult<Seating>(entity, messages, BAD);
		}

		return new DatabaseOperationResult<Seating>(entity, messages, OK);
	}

}
