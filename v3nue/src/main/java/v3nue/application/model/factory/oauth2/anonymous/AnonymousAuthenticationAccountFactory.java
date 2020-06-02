/**
 * 
 */
package v3nue.application.model.factory.oauth2.anonymous;

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
public class AnonymousAuthenticationAccountFactory implements EMFactory<Account, AccountModel> {

	@Autowired
	private AbstractEntityEMFactory abstractEntityEMFactory;

	@Override
	public <X extends Account> X produceEntity(AccountModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		X account = abstractEntityEMFactory.produceEntity(model, clazz);

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
	public <X extends AccountModel> X produceModel(Account account, Class<X> clazz) {
		// TODO Auto-generated method stub
		return abstractEntityEMFactory.produceModel(account, clazz);
	}

}
