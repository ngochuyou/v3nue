/**
 * 
 */
package v3nue.application.model.factory.oauth2.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Personnel;
import v3nue.application.model.factory.converters.PersonnelEMConverter;
import v3nue.application.model.models.PersonnelModel;
import v3nue.core.model.factory.EMFactoryForInheritedModels;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Personnel.class)
public class AdminAuthenticationPersonnelFactory implements EMFactoryForInheritedModels<Personnel, PersonnelModel> {

	@Autowired
	private AdminAuthenticationAccountFactory accountFactory;

	@Autowired
	private PersonnelEMConverter converter;

	@Override
	public Personnel produce(PersonnelModel model) {
		// TODO Auto-generated method stub
		Personnel personnel = accountFactory.convert(accountFactory.produce(model), Personnel.class);

		personnel.setSpecialization(model.getSpecialization());

		return personnel;
	}

	@Override
	public PersonnelModel produce(Personnel entity) {
		// TODO Auto-generated method stub
		PersonnelModel model = accountFactory.convert(accountFactory.produce(entity), PersonnelModel.class);

		model.setSpecialization(entity.getSpecialization());

		return model;
	}

	@Override
	public <X extends Personnel> X convert(Personnel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return converter.convert(instance, clazz);
	}

	@Override
	public <X extends PersonnelModel> X convert(PersonnelModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return converter.convert(instance, clazz);
	}

}
