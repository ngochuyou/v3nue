/**
 * 
 */
package v3nue.application.model.factory.oauth2.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Personnel;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationPersonnelFactory;
import v3nue.application.model.models.PersonnelModel;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Personnel.class)
public class ManagerAuthenticationPersonnelFactory implements EMFactory<Personnel, PersonnelModel> {

	@Autowired
	private AdminAuthenticationPersonnelFactory adminAuthenticationPersonnelFactory;

	@Override
	public <X extends Personnel> X produceEntity(PersonnelModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		return adminAuthenticationPersonnelFactory.produceEntity(model, clazz);
	}

	@Override
	public <X extends PersonnelModel> X produceModel(Personnel entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		return adminAuthenticationPersonnelFactory.produceModel(entity, clazz);
	}
}
