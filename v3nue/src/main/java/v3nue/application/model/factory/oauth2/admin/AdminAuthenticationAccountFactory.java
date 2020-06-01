/**
 * 
 */
package v3nue.application.model.factory.oauth2.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Account;
import v3nue.application.model.models.AccountModel;
import v3nue.core.model.factory.AbstractEntityEMFactory;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.Gender;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Account.class)
public class AdminAuthenticationAccountFactory implements EMFactory<Account, AccountModel> {

	@Autowired
	private AbstractEntityEMFactory abstractFactory;

	@Override
	public <X extends Account> X produce(AccountModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		X account = abstractFactory.produce(model, clazz);

		account.setId(model.getUsername());
		account.setDob(model.getDob());
		account.setEmail(model.getEmail());
		account.setFullname(model.getFullname());

		try {
			account.setGender(Gender.valueOf(model.getGender()));
			account.setRole(AccountRole.valueOf(model.getRole()));
		} catch (RuntimeException exception) {
			account.setGender(null);
			account.setRole(null);
		}

		account.setPassword(model.getPassword());
		account.setPhone(model.getPhone());
		account.setPhoto(model.getPhoto());

		return account;
	}

	@Override
	public <X extends AccountModel> X produce(Account account, Class<X> clazz) {
		// TODO Auto-generated method stub
		X model = abstractFactory.produce(account, clazz);

		model.setUsername(account.getId());
		model.setDob(account.getDob());
		model.setEmail(account.getEmail());
		model.setFullname(account.getFullname());
		model.setGender(account.getGender().toString());
		model.setRole(account.getRole().toString());
		model.setPassword(null);
		model.setPhone(account.getPhone());
		model.setPhoto(account.getPhoto());

		return model;
	}

}
