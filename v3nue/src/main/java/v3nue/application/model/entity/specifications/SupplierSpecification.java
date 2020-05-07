/**
 * 
 */
package v3nue.application.model.entity.specifications;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Supplier;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecification;
import v3nue.core.model.entity.specification.EntityValidationResult;
import v3nue.core.utils.StringUtil;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { Supplier.class })
public class SupplierSpecification extends CompositeSpecification<Supplier> {

	@Override
	public EntityValidationResult<Supplier> isSatisfiedBy(Supplier entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<String, String>();
		int status = OK;
		String phone = entity.getPhone();

		if (phone == null || phone.length() < 0 || phone.length() > 15 || !StringUtil.isDigits(phone)) {
			messages.put("phone",
					"Phone number must not be empty, can only contain digits and have 15 characters max.");
			status = BAD;
		}

		String email = entity.getEmail();

		if (!StringUtil.isEmpty(email) && !StringUtil.isEmail(email)) {
			messages.put("email", "Invalid email address.");
			status = BAD;
		}

		return new EntityValidationResult<Supplier>(entity, messages, status);
	}

}
