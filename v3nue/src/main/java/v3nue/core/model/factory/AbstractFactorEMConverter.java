/**
 * 
 */
package v3nue.core.model.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.models.AbstractFactorModel;
import v3nue.core.model.AbstractFactor;

/**
 * @author Ngoc Huy
 *
 */
@Component
public class AbstractFactorEMConverter implements EMConvertMethod<AbstractFactor, AbstractFactorModel> {

	@Autowired
	private AbstractEMConverter superConverter;

	@Override
	public <X extends AbstractFactor> X convert(AbstractFactor instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = superConverter.convert(instance, clazz);

			x.setId(instance.getId());
			x.setName(instance.getName());
			x.setCreatedBy(instance.getCreatedBy());

			return x;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <X extends AbstractFactorModel> X convert(AbstractFactorModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = superConverter.convert(instance, clazz);

			x.setName(instance.getName());
			x.setCreatedBy(instance.getCreatedBy());

			return x;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return null;
		}
	}

}
