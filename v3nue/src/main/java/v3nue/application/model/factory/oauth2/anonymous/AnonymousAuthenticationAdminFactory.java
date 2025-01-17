/**
 * 
 */
package v3nue.application.model.factory.oauth2.anonymous;

import java.lang.reflect.InvocationTargetException;

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
public class AnonymousAuthenticationAdminFactory implements EMFactory<Admin, AdminModel> {

	@Override
	public <X extends Admin> X produceEntity(AdminModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			return clazz.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <X extends AdminModel> X produceModel(Admin entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			return clazz.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
