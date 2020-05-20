/**
 * 
 */
package v3nue.application.model.factory.converters;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Customer;
import v3nue.application.model.models.CustomerModel;
import v3nue.core.model.factory.EMConvertMethod;

/**
 * @author Ngoc Huy
 *
 */
@Component
public class CustomerEMConverter implements EMConvertMethod<Customer, CustomerModel> {

	private AccountEMConverter superConverter = new AccountEMConverter();

	@Override
	public <X extends Customer> X convert(Customer instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = superConverter.convert(instance, clazz);
			
			x.setPrestigePoint(instance.getPrestigePoint());
			
			return x;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <X extends CustomerModel> X convert(CustomerModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = superConverter.convert(instance, clazz);
			
			x.setPrestigePoint(instance.getPrestigePoint());
			
			return x;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
