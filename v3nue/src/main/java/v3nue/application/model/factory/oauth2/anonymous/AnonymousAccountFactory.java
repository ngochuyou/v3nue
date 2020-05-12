/**
 * 
 */
package v3nue.application.model.factory.oauth2.anonymous;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Account;
import v3nue.application.model.factory.converters.AccountEMConverter;
import v3nue.application.model.models.AccountModel;
import v3nue.core.model.factory.EMFactoryForInheritedModels;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Account.class)
public class AnonymousAccountFactory implements EMFactoryForInheritedModels<Account, AccountModel> {

	@Override
	public Account produce(AccountModel model) {
		// TODO Auto-generated method stub
		Account account = new Account();

		account.setId(model.getUsername());
		account.setActive(model.isActive());
		account.setCreatedDate(model.getCreatedDate());
		account.setDob(model.getDob());
		account.setEmail(model.getEmail());
		account.setFullname(model.getFullname());
		account.setGender(model.getGender());
		account.setPassword(model.getPassword());
		account.setPhone(model.getPhone());
		account.setPhoto(model.getPhoto());
		account.setRole(model.getRole());
		account.setUpdatedDate(model.getUpdatedDate());

		return account;
	}

	@Override
	public AccountModel produce(Account account) {
		// TODO Auto-generated method stub
		AccountModel model = new AccountModel();

		model.setUsername(account.getId());
		model.setActive(account.isActive());
		model.setPhoto(account.getPhoto());
		model.setRole(account.getRole());
		model.setGender(account.getGender());
		model.setFullname(account.getFullname());
		model.setPhone(account.getPhone());
		model.setEmail(account.getEmail());
		model.setCreatedDate(null);
		model.setDob(null);
		model.setPassword(null);
		model.setUpdatedDate(null);

		return model;
	}

	@Override
	public <X extends Account> X convert(Account instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return new AccountEMConverter().convert(instance, clazz);
	}

	@Override
	public <X extends AccountModel> X convert(AccountModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return new AccountEMConverter().convert(instance, clazz);
	}

}
