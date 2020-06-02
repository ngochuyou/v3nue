/**
 * 
 */
package v3nue.application.model.factory.oauth2.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Admin;
import v3nue.application.model.models.AdminModel;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Admin.class)
public class AdminAuthenticationAdminFactory implements EMFactory<Admin, AdminModel> {

	@Autowired
	private AdminAuthenticationAccountFactory accountFactory;

	@Override
	public <X extends Admin> X produceEntity(AdminModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		return accountFactory.produceEntity(model, clazz);
	}

	@Override
	public <X extends AdminModel> X produceModel(Admin entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		return accountFactory.produceModel(entity, clazz);
	}

}
