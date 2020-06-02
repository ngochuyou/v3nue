/**
 * 
 */
package v3nue.application.model.factory.oauth2.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Mandatory;
import v3nue.application.model.models.MandatoryModel;
import v3nue.core.model.factory.AbstractFactorEMFactory;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;
import v3nue.core.utils.CollectionUtils;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Mandatory.class)
public class AdminAuthenticationMandatoryFactory implements EMFactory<Mandatory, MandatoryModel> {

	@Autowired
	private AbstractFactorEMFactory factorFactory;

	@Override
	public <X extends Mandatory> X produceEntity(MandatoryModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		X factor = factorFactory.produceEntity(model, clazz);

		factor.setPrice(model.getPrice());
		factor.setSuppliers(CollectionUtils.toSet(model.getSuppliers()));
		factor.setType(model.getType());

		return factor;
	}

	@Override
	public <X extends MandatoryModel> X produceModel(Mandatory factor, Class<X> clazz) {
		// TODO Auto-generated method stub
		X model = factorFactory.produceModel(factor, clazz);

		model.setPrice(factor.getPrice());
		model.setSuppliers(CollectionUtils.toList(factor.getSuppliers()));
		model.setType(factor.getType());

		return model;
	}

}
