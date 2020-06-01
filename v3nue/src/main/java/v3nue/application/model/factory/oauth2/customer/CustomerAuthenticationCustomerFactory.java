/**
 * 
 */
package v3nue.application.model.factory.oauth2.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Customer;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationCustomerFactory;
import v3nue.application.model.models.CustomerModel;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.Factory;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = Customer.class)
public class CustomerAuthenticationCustomerFactory implements EMFactory<Customer, CustomerModel> {

	@Autowired
	private AdminAuthenticationCustomerFactory customerFactory;

	@Override
	public <X extends Customer> X produce(CustomerModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		return customerFactory.produce(model, clazz);
	}

	@Override
	public <X extends CustomerModel> X produce(Customer entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		return customerFactory.produce(entity, clazz);
	}

}
