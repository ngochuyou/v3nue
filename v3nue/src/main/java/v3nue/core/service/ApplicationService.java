/**
 * 
 */
package v3nue.core.service;

import v3nue.core.model.AbstractEntity;

/**
 * @author Ngoc Huy
 *
 */
public interface ApplicationService<T extends AbstractEntity> {

	<X extends T> X doMandatory(X instance);
	
}
