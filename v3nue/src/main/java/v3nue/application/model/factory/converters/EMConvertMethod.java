/**
 * 
 */
package v3nue.application.model.factory.converters;

import v3nue.application.model.models.AbstractModel;
import v3nue.core.model.AbstractEntity;

/**
 * @author Ngoc Huy
 *
 */
public interface EMConvertMethod<T extends AbstractEntity, M extends AbstractModel> {

	public <X extends T> X convert(T instance, Class<X> clazz);
	
	public <X extends M> X convert(M instance, Class<X> clazz);
	
}
