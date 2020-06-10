/**
 * 
 */
package v3nue.application.model.factory.oauth2.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Contract;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationContractFactory;
import v3nue.application.model.models.ContractModel;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Contract.class)
public class ManagerAuthenticationContractFactory implements EMFactory<Contract, ContractModel> {

	@Autowired
	private AdminAuthenticationContractFactory adminAuthenticationContractFactory;

	@Override
	public <X extends Contract> X produceEntity(ContractModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		return adminAuthenticationContractFactory.produceEntity(model, clazz);
	}

	@Override
	public <X extends ContractModel> X produceModel(Contract entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		return adminAuthenticationContractFactory.produceModel(entity, clazz);
	}

}
