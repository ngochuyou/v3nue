/**
 * 
 */
package v3nue.application.model.factory.oauth2.anonymous;

import java.lang.reflect.InvocationTargetException;

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
public class AnonymousAuthenticationCustomerFactory implements EMFactory<Customer, CustomerModel> {

	@Override
	public <X extends Customer> X produceEntity(CustomerModel model, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			return clazz.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <X extends CustomerModel> X produceModel(Customer entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			return clazz.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
