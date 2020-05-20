/**
 * 
 */
package v3nue.application.model.factory.oauth2.anonymous;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import v3nue.application.model.factory.OAuth2RoleBased;
import v3nue.core.ApplicationManager;
import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.EMFactoryManager;
import v3nue.core.model.factory.Factory;
import v3nue.core.utils.AccountRole;

/**
 * @author Ngoc Huy
 *
 */
@OAuth2RoleBased(role = AccountRole.Anonymous)
@Component
@Order(value = 5)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AnonymousAuthenticationEMFactoryManager implements EMFactoryManager, ApplicationManager {

	private Map<Class<? extends AbstractEntity>, EMFactory<? extends AbstractEntity, ? extends Model>> factoryMap;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		logger.info("Initializing AnonymousAuthenticationEMFactoryManager");
		factoryMap = new HashMap<>();
		contextUtils.getComponentStream(this.getClass().getPackageName(),
				Stream.of(new AssignableTypeFilter(EMFactory.class), new AnnotationTypeFilter(Factory.class))
						.collect(Collectors.toSet()),
				null, false).forEach(bean -> {
					try {
						Class<? extends EMFactory> clazz = (Class<? extends EMFactory>) Class
								.forName(bean.getBeanClassName());
						Factory anno = clazz.getDeclaredAnnotation(Factory.class);

						factoryMap.put(anno.entity(), (EMFactory) context.getBean(clazz));
						logger.info(clazz.getName() + " has been assigned for producing Models of type "
								+ anno.entity().getName() + " for AnonymousAuthentication");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		entityTree.forEach(tree -> {
			Class<? extends AbstractEntity> clazz = tree.getNode();

			if (factoryMap.get(clazz) == null) {
				logger.info(defaultEMFactory.getClass().getName() + " has been assigned for producing Models of type "
						+ clazz.getName() + " for AnonymousAuthentication");
				factoryMap.put(clazz, defaultEMFactory);
			}
		});
		logger.info("Finished initializing AnonymousAuthenticationEMFactoryManager");
	}

	@Override
	public <T extends AbstractEntity, M extends Model> EMFactory<T, M> getEMFactory(Class<T> clazz) {
		// TODO Auto-generated method stub
		return (EMFactory<T, M>) this.factoryMap.get(clazz);
	}

}
