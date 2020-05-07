/**
 * 
 */
package v3nue.core;

import org.springframework.context.ApplicationContext;

import v3nue.core.utils.ApplicationContextUtils;
import v3nue.core.utils.ClassReflector;

/**
 * Classes implement from this interface represent themselfs as a
 * ApplicationManager, mostly manage beans, usages of patterns, etc..
 * <p>
 * Every ApplicationManagers will have to initialize their contexts.
 * </p>
 * 
 * @author Ngoc Huy
 *
 */
public interface ApplicationManager {

	final ApplicationContext context = ApplicationContextUtils.getContext();

	final ApplicationContextUtils contextUtils = context.getBean(ApplicationContextUtils.class);

	final ClassReflector reflector = context.getBean(ClassReflector.class);
	
	void initialize();

}
