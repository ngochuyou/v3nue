/**
 * 
 */
package v3nue.application.model.factory.oauth2.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Customer;
import v3nue.application.model.models.CustomerModel;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Customer.class)
public class AdminAuthenticationCustomerFactory implements EMFactory<Customer, CustomerModel> {

	@Autowired
	private AdminAuthenticationAccountFactory accountFactory;

	@Override
	public <X extends Customer> X produceEntity(CustomerModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		X customer = accountFactory.produceEntity(model, clazz);

		customer.setPrestigePoint(model.getPrestigePoint());

		return customer;
	}

	@Override
	public <X extends CustomerModel> X produceModel(Customer customer, Class<X> clazz) {
		// TODO Auto-generated method stub
		X model = accountFactory.produceModel(customer, clazz);

		model.setPrestigePoint(customer.getPrestigePoint());

		return model;
	}

}
