/**
 * 
 */
package v3nue.core.model.factory;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;

/**
 * @author Ngoc Huy
 *
 */
public interface EMFactoryManager {

	public <T extends AbstractEntity, M extends Model> EMFactory<T, M> getEMFactory(Class<T> clazz);

}
