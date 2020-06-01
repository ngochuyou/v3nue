/**
 * 
 */
package v3nue.application.model.factory.oauth2.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Personnel;
import v3nue.application.model.models.PersonnelModel;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Personnel.class)
public class AdminAuthenticationPersonnelFactory implements EMFactory<Personnel, PersonnelModel> {

	@Autowired
	private AdminAuthenticationAccountFactory accountFactory;

	@Override
	public <X extends Personnel> X produce(PersonnelModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		X personnel = accountFactory.produce(model, clazz);

		personnel.setSpecialization(model.getSpecialization());

		return personnel;
	}

	@Override
	public <X extends PersonnelModel> X produce(Personnel entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		X model = accountFactory.produce(entity, clazz);

		model.setSpecialization(entity.getSpecialization());

		return model;
	}

}
