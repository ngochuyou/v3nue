/**
 * 
 */
package v3nue.application.model.factory.oauth2.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Account;
import v3nue.application.model.entities.Contract;
import v3nue.application.model.models.AccountModel;
import v3nue.application.model.models.ContractModel;
import v3nue.core.model.factory.AbstractFactorEMFactory;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;
import v3nue.core.utils.CollectionUtils;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Contract.class)
public class AdminAuthenticationContractFactory implements EMFactory<Contract, ContractModel> {

	@Autowired
	private AbstractFactorEMFactory abstractFactorFactory;

	@Autowired
	private AdminAuthenticationAccountFactory accountFactory;

	@Override
	public <X extends Contract> X produceEntity(ContractModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		X x = abstractFactorFactory.produceEntity(model, clazz);

		x.setAgreedAmount(model.getAgreedAmount());
		x.setDepositAmount(model.getDepositAmount());
		x.setTotalAmount(model.getTotalAmount());
		x.setDescription(model.getDescription());
		x.setSupervisor(accountFactory.produceEntity(model.getSupervisor(), Account.class));
		x.setBooking(model.getBooking());
		x.setSeatingsDetails(CollectionUtils.toSet(model.getSeatingsDetails()));
		x.setMandatoriesDetails(CollectionUtils.toSet(model.getMandatoriesDetails()));
		x.setFoodsAndDrinksDetails(CollectionUtils.toSet(model.getFoodsAndDrinksDetails()));

		return x;
	}

	@Override
	public <X extends ContractModel> X produceModel(Contract entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		X x = abstractFactorFactory.produceModel(entity, clazz);

		x.setAgreedAmount(entity.getAgreedAmount());
		x.setDepositAmount(entity.getDepositAmount());
		x.setTotalAmount(entity.getTotalAmount());
		x.setDescription(entity.getDescription());
		x.setSupervisor(accountFactory.produceModel(entity.getSupervisor(), AccountModel.class));
		x.setBooking(entity.getBooking());
		x.setSeatingsDetails(CollectionUtils.toList(entity.getSeatingsDetails()));
		x.setMandatoriesDetails(CollectionUtils.toList(entity.getMandatoriesDetails()));
		x.setFoodsAndDrinksDetails(CollectionUtils.toList(entity.getFoodsAndDrinksDetails()));

		return x;
	}

}
