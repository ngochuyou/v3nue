/**
 * 
 */
package v3nue.application.model.factory;

import v3nue.core.ApplicationManager;
import v3nue.core.model.factory.EMFactoryManager;

/**
 * @author Ngoc Huy
 *
 */
public interface EMFactoryManagerProvider extends ApplicationManager {

	EMFactoryManager getEMFactoryManager();

}
