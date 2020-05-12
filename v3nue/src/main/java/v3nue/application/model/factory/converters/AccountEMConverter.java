/**
 * 
 */
package v3nue.application.model.factory.converters;

import v3nue.application.model.entities.Account;
import v3nue.application.model.models.AccountModel;

/**
 * @author Ngoc Huy
 *
 */
public class AccountEMConverter implements EMConvertMethod<Account, AccountModel> {

	private AbstractEMConverter superConverter = new AbstractEMConverter();

	@Override
	public <X extends Account> X convert(Account instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = superConverter.convert(instance, clazz);

			x.setAuthorities(instance.getAuthorities());
			x.setDob(instance.getDob());
			x.setEmail(instance.getEmail());
			x.setFullname(instance.getFullname());
			x.setGender(instance.getGender());
			x.setId(instance.getId());
			x.setPassword(instance.getPassword());
			x.setPhone(instance.getPhone());
			x.setPhoto(instance.getPhoto());
			x.setRole(instance.getRole());

			return x;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <X extends AccountModel> X convert(AccountModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = superConverter.convert(instance, clazz);

			x.setDob(instance.getDob());
			x.setEmail(instance.getEmail());
			x.setFullname(instance.getFullname());
			x.setGender(instance.getGender());
			x.setUsername(instance.getUsername());
			x.setPassword(instance.getPassword());
			x.setPhone(instance.getPhone());
			x.setPhoto(instance.getPhoto());
			x.setRole(instance.getRole());

			return x;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
