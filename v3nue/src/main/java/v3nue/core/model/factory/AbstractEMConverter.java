/**
 * 
 */
package v3nue.core.model.factory;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;

import v3nue.application.model.models.AbstractModel;
import v3nue.core.model.AbstractEntity;

/**
 * @author Ngoc Huy
 *
 */
@Component
public class AbstractEMConverter implements EMConvertMethod<AbstractEntity, AbstractModel> {

	@Override
	public <X extends AbstractEntity> X convert(AbstractEntity instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = clazz.getConstructor().newInstance();

			x.setActive(instance.isActive());
			x.setCreatedDate(instance.getCreatedDate());
			x.setUpdatedDate(instance.getUpdatedDate());
			
			return x;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <X extends AbstractModel> X convert(AbstractModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		try {
			X x = clazz.getConstructor().newInstance();

			x.setActive(instance.isActive());
			x.setCreatedDate(instance.getCreatedDate());
			x.setUpdatedDate(instance.getUpdatedDate());
			
			return x;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
