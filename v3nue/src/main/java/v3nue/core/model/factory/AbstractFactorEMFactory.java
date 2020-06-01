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
@Factory(entity = AbstractFactor.class)
public class AbstractFactorEMFactory implements EMFactory<AbstractFactor, AbstractFactorModel> {

	@Autowired
	private AbstractEntityEMFactory abstractFactory;

	@Override
	public <X extends AbstractFactor> X produce(AbstractFactorModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		X factor = abstractFactory.produce(model, clazz);

		factor.setName(model.getName());
		factor.setCreatedBy(model.getCreatedBy());

		return factor;
	}

	@Override
	public <X extends AbstractFactorModel> X produce(AbstractFactor entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		X model = abstractFactory.produce(entity, clazz);

		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setCreatedBy(entity.getCreatedBy());

		return model;
	}

}
