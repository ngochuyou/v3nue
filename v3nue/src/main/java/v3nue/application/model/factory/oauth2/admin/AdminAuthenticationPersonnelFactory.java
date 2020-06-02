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
	public <X extends Personnel> X produceEntity(PersonnelModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		X personnel = accountFactory.produceEntity(model, clazz);

		personnel.setSpecialization(model.getSpecialization());

		return personnel;
	}

	@Override
	public <X extends PersonnelModel> X produceModel(Personnel entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		X model = accountFactory.produceModel(entity, clazz);

		model.setSpecialization(entity.getSpecialization());

		return model;
	}

}
