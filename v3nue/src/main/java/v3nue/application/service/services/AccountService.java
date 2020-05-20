/**
 * 
 */
package v3nue.application.service.services;

import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import v3nue.application.model.entities.Account;
import v3nue.core.service.ApplicationService;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.Gender;
import v3nue.core.utils.StringUtil;

/**
 * @author Ngoc Huy
 *
 */
@Service
public class AccountService implements ApplicationService<Account> {

	@Override
	public <T extends Account> T doMandatory(T account) {
		account.setCreatedDate(new Date());
		account.setActive(true);

		if (account.getGender() == null) {
			account.setGender(Gender.UNKNOWN);
		}

		account.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));

		if (StringUtil.isEmpty(account.getPhoto())) {
			account.setPhoto("default.jpg");
		}

		if (account.getRole() == null) {
			account.setRole(AccountRole.Customer);
		}

		return account;
	}

}
