/**
 * 
 */
package v3nue.application.model.factory.oauth2.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Mandatory;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationMandatoryFactory;
import v3nue.application.model.models.MandatoryModel;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Mandatory.class)
public class ManagerAuthenticationMandatoryFactory implements EMFactory<Mandatory, MandatoryModel> {

	@Autowired
	private AdminAuthenticationMandatoryFactory adminAuthenticationMandatoryFactory;

	@Override
	public <X extends Mandatory> X produceEntity(MandatoryModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		return adminAuthenticationMandatoryFactory.produceEntity(model, clazz);
	}

	@Override
	public <X extends MandatoryModel> X produceModel(Mandatory entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		return adminAuthenticationMandatoryFactory.produceModel(entity, clazz);
	}

}
