/**
 * 
 */
package v3nue.application.model.factory.oauth2.anonymous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Customer;
import v3nue.application.model.factory.converters.CustomerEMConverter;
import v3nue.application.model.models.CustomerModel;
import v3nue.core.model.factory.EMFactoryForInheritedModels;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Customer.class)
public class AnonymousCustomerFactory implements EMFactoryForInheritedModels<Customer, CustomerModel> {

	@Autowired
	private AnonymousAccountFactory superFactory;

	@Override
	public Customer produce(CustomerModel model) {
		// TODO Auto-generated method stub
		Customer customer = superFactory.convert(superFactory.produce(model), Customer.class);

		customer.setPrestigePoint(model.getPrestigePoint());

		return customer;
	}

	@Override
	public CustomerModel produce(Customer entity) {
		// TODO Auto-generated method stub
		CustomerModel model = superFactory.convert(superFactory.produce(entity), CustomerModel.class);

		model.setPrestigePoint(entity.getPrestigePoint());

		return model;
	}

	@Override
	public <X extends Customer> X convert(Customer instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return new CustomerEMConverter().convert(instance, clazz);
	}

	@Override
	public <X extends CustomerModel> X convert(CustomerModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return new CustomerEMConverter().convert(instance, clazz);
	}

}
