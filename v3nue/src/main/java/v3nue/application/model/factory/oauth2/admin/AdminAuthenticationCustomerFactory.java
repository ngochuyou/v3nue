/**
 * 
 */
package v3nue.application.model.factory.oauth2.admin;

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
public class AdminAuthenticationCustomerFactory implements EMFactoryForInheritedModels<Customer, CustomerModel> {
	
	@Autowired
	private AdminAuthenticationAccountFactory accountFactory;
	
	@Autowired
	private CustomerEMConverter converter;
	
	@Override
	public Customer produce(CustomerModel model) {
		// TODO Auto-generated method stub
		Customer customer = accountFactory.convert(accountFactory.produce(model), Customer.class);
		
		customer.setPrestigePoint(model.getPrestigePoint());
		
		return customer;
	}

	@Override
	public CustomerModel produce(Customer customer) {
		// TODO Auto-generated method stub
		CustomerModel model = accountFactory.convert(accountFactory.produce(customer), CustomerModel.class);
		
		model.setPrestigePoint(customer.getPrestigePoint());
		
		return model;
	}

	@Override
	public <X extends Customer> X convert(Customer instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return converter.convert(instance, clazz);
	}

	@Override
	public <X extends CustomerModel> X convert(CustomerModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return converter.convert(instance, clazz);
	}

}
