/**
 * 
 */
package v3nue.application.model.entity.specifications;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Account;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecification;
import v3nue.core.model.entity.specification.EntityValidationResult;
import v3nue.core.utils.StringUtil;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { Account.class })
public class AccountSpecification extends CompositeSpecification<Account> {

	@Override
	public EntityValidationResult<Account> isSatisfiedBy(Account entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<>();
		String id = entity.getId();
		int status = OK;

		if (id == null || id.length() < 8 || id.length() > 255) {
			messages.put("id", "Username length must be between 8 and 255.");
			status = BAD;
		}

		String phone = entity.getPhone();

		if (phone == null || phone.length() < 0 || phone.length() > 15 || !StringUtil.isDigits(phone)) {
			messages.put("phone",
					"Phone number must not be empty, can only contain digits and have 15 characters max.");
			status = BAD;
		}

		String email = entity.getEmail();

		if (email == null || !StringUtil.isEmail(email)) {
			messages.put("email", "Invalid email address.");
			status = BAD;
		}

		String password = entity.getPassword();

		if (password == null || !StringUtil.isBCrypt(password)) {
			messages.put("password", "Invalid password.");
			status = BAD;
		}

		String fullname = entity.getFullname();

		if (fullname == null || fullname.length() < 0 || fullname.length() > 255) {
			messages.put("fullname", "Fullname length must be between 1 and 255.");
			status = BAD;
		}

		if (entity.getGender() == null) {
			messages.put("gender", "Gender must be presented.");
			status = BAD;
		}

		if (entity.getRole() == null) {
			messages.put("role", "Account's role must be presented.");
			status = BAD;
		}

		Date dob = entity.getDob();

		if (dob == null || dob.after(new Date())) {
			messages.put("dob", "Invalid birth date.");
			status = BAD;
		}

		return new EntityValidationResult<Account>(entity, messages, status);
	}

}
