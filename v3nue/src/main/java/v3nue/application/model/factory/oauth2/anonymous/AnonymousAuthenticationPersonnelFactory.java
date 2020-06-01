/**
 * 
 */
package v3nue.application.model.factory.oauth2.anonymous;

import java.lang.reflect.InvocationTargetException;

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
public class AnonymousAuthenticationPersonnelFactory implements EMFactory<Personnel, PersonnelModel> {

	@Autowired
	private AnonymousAuthenticationAccountFactory superFactory;

	@Override
	public <X extends Personnel> X produce(PersonnelModel model, Class<X> clazz) {
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
	public <X extends PersonnelModel> X produce(Personnel entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		X model = superFactory.produce(entity, clazz);

		model.setSpecialization(entity.getSpecialization());

		return model;
	}

}
