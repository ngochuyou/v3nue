/**
 * 
 */
package v3nue.application.model.entity.specifications;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Customer;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecification;
import v3nue.core.model.entity.specification.EntityValidationResult;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { Customer.class })
public class CustomerSpecification extends CompositeSpecification<Customer> {

	@Override
	public EntityValidationResult<Customer> isSatisfiedBy(Customer entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<String, String>();
		int status = OK;

		if (entity.getPrestigePoint() < 0) {
			messages.put("prestigePoint", "Prestige point can not be negative.");
			status = BAD;
		}

		return new EntityValidationResult<Customer>(entity, messages, status);
	}

}
