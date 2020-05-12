/**
 * 
 */
package v3nue.application.model.factory.converters;

import v3nue.application.model.entities.Admin;
import v3nue.application.model.models.AdminModel;

/**
 * @author Ngoc Huy
 *
 */
public class AdminEMConverter implements EMConvertMethod<Admin, AdminModel> {

	private AccountEMConverter superConverter = new AccountEMConverter();
	
	@Override
	public <X extends Admin> X convert(Admin instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			return superConverter.convert(instance, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <X extends AdminModel> X convert(AdminModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			return superConverter.convert(instance, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
