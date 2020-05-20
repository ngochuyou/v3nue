/**
 * 
 */
package v3nue.application.model.factory.oauth2.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Account;
import v3nue.application.model.factory.converters.AccountEMConverter;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationAccountFactory;
import v3nue.application.model.models.AccountModel;
import v3nue.core.model.factory.EMFactoryForInheritedModels;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Account.class)
public class CustomerAuthenticationAccountFactory implements EMFactoryForInheritedModels<Account, AccountModel> {

	@Autowired
	private AdminAuthenticationAccountFactory accountFactory;

	@Autowired
	private AccountEMConverter converter;

	@Override
	public Account produce(AccountModel model) {
		// TODO Auto-generated method stub
		return accountFactory.produce(model);
	}

	@Override
	public AccountModel produce(Account account) {
		// TODO Auto-generated method stub
		return accountFactory.produce(account);
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
