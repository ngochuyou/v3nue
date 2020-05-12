/**
 * 
 */
package v3nue.application.model.factory.converters;

import v3nue.application.model.entities.Personnel;
import v3nue.application.model.models.PersonnelModel;

/**
 * @author Ngoc Huy
 *
 */
public class PersonnelEMConverter implements EMConvertMethod<Personnel, PersonnelModel> {

	private AccountEMConverter superConverter = new AccountEMConverter();

	@Override
	public <X extends Personnel> X convert(Personnel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = superConverter.convert(instance, clazz);

			x.setSpecialization(instance.getSpecialization());

			return x;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public <X extends PersonnelModel> X convert(PersonnelModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = superConverter.convert(instance, clazz);

			x.setSpecialization(instance.getSpecialization());

			return x;
		} catch (Exception e) {
			return null;
		}
	}

}
