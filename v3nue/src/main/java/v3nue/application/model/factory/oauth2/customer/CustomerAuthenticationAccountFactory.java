/**
 * 
 */
package v3nue.application.model.factory.oauth2.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Account;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationAccountFactory;
import v3nue.application.model.models.AccountModel;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Account.class)
public class CustomerAuthenticationAccountFactory implements EMFactory<Account, AccountModel> {

	@Autowired
	private AdminAuthenticationAccountFactory accountFactory;

	@Override
	public <X extends Account> X produce(AccountModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		return accountFactory.produce(model, clazz);
	}

	@Override
	public <X extends AccountModel> X produce(Account account, Class<X> clazz) {
		// TODO Auto-generated method stub
		return accountFactory.produce(account, clazz);
	}

}
