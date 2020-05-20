/**
 * 
 */
package v3nue.application.model.factory.oauth2.anonymous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Account;
import v3nue.application.model.factory.converters.AccountEMConverter;
import v3nue.application.model.models.AccountModel;
import v3nue.core.model.factory.AbstractEntityEMFactory;
import v3nue.core.model.factory.EMFactoryForInheritedModels;
import v3nue.core.model.factory.Factory;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.Gender;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Account.class)
public class AnonymousAuthenticationAccountFactory implements EMFactoryForInheritedModels<Account, AccountModel> {

	@Autowired
	private AbstractEntityEMFactory abstractEntityEMFactory;

	@Autowired
	private AccountEMConverter converter;

	@Override
	public Account produce(AccountModel model) {
		// TODO Auto-generated method stub
		Account account = abstractEntityEMFactory.convert(abstractEntityEMFactory.produce(model), Account.class);

		account.setId(model.getUsername());
		account.setActive(model.isActive());
		account.setCreatedDate(model.getCreatedDate());
		account.setDob(model.getDob());
		account.setEmail(model.getEmail());
		account.setFullname(model.getFullname());

		try {
			account.setGender(Gender.valueOf(model.getGender()));
			account.setRole(AccountRole.valueOf(model.getRole()));
		} catch (Exception e) {
			account.setGender(null);
			account.setRole(null);
		}

		account.setPassword(model.getPassword());
		account.setPhone(model.getPhone());
		account.setPhoto(model.getPhoto());
		account.setUpdatedDate(model.getUpdatedDate());

		return account;
	}

	@Override
	public AccountModel produce(Account account) {
		// TODO Auto-generated method stub
		return new AccountModel();
	}

	@Override
	public <X extends Account> X convert(Account instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return converter.convert(instance, clazz);
	}

	@Override
	public <X extends AccountModel> X convert(AccountModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return converter.convert(instance, clazz);
	}

}
