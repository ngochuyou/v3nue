/**
 * 
 */
package v3nue.application.model.factory.oauth2.anonymous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Admin;
import v3nue.application.model.factory.converters.AdminEMConverter;
import v3nue.application.model.models.AdminModel;
import v3nue.core.model.factory.EMFactoryForInheritedModels;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Admin.class)
public class AnonymousAdminFactory implements EMFactoryForInheritedModels<Admin, AdminModel> {

	@Autowired
	private AnonymousAccountFactory superFactory;
	
	@Override
	public Admin produce(AdminModel model) {
		// TODO Auto-generated method stub
		return superFactory.convert(superFactory.produce(model), Admin.class);
	}

	@Override
	public AdminModel produce(Admin entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X extends Admin> X convert(Admin instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return new AdminEMConverter().convert(instance, clazz);
	}

	@Override
	public <X extends AdminModel> X convert(AdminModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return new AdminEMConverter().convert(instance, clazz);
	}

}
