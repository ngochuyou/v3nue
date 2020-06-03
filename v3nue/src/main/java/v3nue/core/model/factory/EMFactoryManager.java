/**
 * 
 */
package v3nue.core.model.factory;

import v3nue.core.ModelManager;
import v3nue.core.model.AbstractEntity;
import v3nue.core.model.EntityInheritanceTree;
import v3nue.core.model.Model;
import v3nue.core.model.exceptions.NoFactoryException;
import v3nue.core.utils.ApplicationContextUtils;

/**
 * @author Ngoc Huy
 *
 */
public interface EMFactoryManager {

	<T extends AbstractEntity, M extends Model> EMFactory<T, M> getEMFactory(Class<T> clazz) throws NoFactoryException;

	EntityInheritanceTree entityTree = ApplicationContextUtils.getContext().getBean(ModelManager.class).getEntityTree();

	DefaultEMFactory defaultEMFactory = ApplicationContextUtils.getContext().getBean(DefaultEMFactory.class);

}
