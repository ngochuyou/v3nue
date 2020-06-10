/**
 * 
 */
package v3nue.application.model.factory.oauth2.employee;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import v3nue.application.model.factory.OAuth2RoleBased;
import v3nue.core.ApplicationManager;
import v3nue.core.model.AbstractEntity;
import v3nue.core.model.AbstractFactor;
import v3nue.core.model.Model;
import v3nue.core.model.annotations.Relation;
import v3nue.core.model.exceptions.NoFactoryException;
import v3nue.core.model.exceptions.NonStandardModelException;
import v3nue.core.model.factory.AbstractFactorEMFactory;
import v3nue.core.model.factory.DefaultEMFactory;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.EMFactoryManager;
import v3nue.core.model.factory.Factory;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.Constants;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Order(value = 7)
@OAuth2RoleBased(role = AccountRole.Employee)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EmployeeAuthenticationEMFactoryManager implements EMFactoryManager, ApplicationManager {

	private Map<Class<? extends AbstractEntity>, EMFactory<? extends AbstractEntity, ? extends Model>> factoryMap;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		logger.info("Initializing ManagerAuthenticationEMFactoryManager");
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
								+ anno.entity().getName() + " for ManagerAuthentication");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		entityTree.forEach(tree -> {
			Class<? extends AbstractEntity> clazz = tree.getNode();

			if (factoryMap.get(clazz) == null) {
				logger.info(defaultEMFactory.getClass().getName() + " has been assigned for producing Models of type "
						+ clazz.getName() + " for ManagerAuthentication");
				factoryMap.put(clazz, defaultEMFactory);
			}
		});
		logger.info(AbstractFactorEMFactory.class.getName() + " has been assigned for producing Models of type "
				+ AbstractFactor.class.getName() + " for ManagerAuthentication");
		factoryMap.put(AbstractFactor.class, context.getBean(AbstractFactorEMFactory.class));
		contextUtils.getComponentStream(Constants.modelPackage,
				new HashSet<>(Set.of(new AssignableTypeFilter(Model.class))),
				new HashSet<>(Set.of(new AssignableTypeFilter(AbstractEntity.class))), false).forEach(bean -> {
					try {
						Class<? extends Model> clazz = (Class<? extends Model>) Class.forName(bean.getBeanClassName());
						Relation anno = clazz.getDeclaredAnnotation(Relation.class);

						if (anno == null) {
							throw new NonStandardModelException(clazz.getName());
						}

						Class<? extends AbstractEntity> entityClass = anno.relation();

						if (factoryMap.get(entityClass) instanceof DefaultEMFactory) {
							logger.info("Removing " + defaultEMFactory.getClass().getName()
									+ " for producing Models of type " + entityClass.getName()
									+ " for ManagerAuthentication");
							factoryMap.put(entityClass, null);
						}
					} catch (Exception e) {
						e.printStackTrace();
						SpringApplication.exit(context);
					}
				});
		logger.info("Finished initializing ManagerAuthenticationEMFactoryManager");
	}

	@Override
	public <T extends AbstractEntity, M extends Model> EMFactory<T, M> getEMFactory(Class<T> clazz)
			throws NoFactoryException {
		// TODO Auto-generated method stub
		EMFactory<T, M> factory = (EMFactory<T, M>) this.factoryMap.get(clazz);

		if (factory == null) {
			throw new NoFactoryException("No Factory found for " + clazz.getName() + " with ManagerAuthentication");
		}

		return factory;
	}
}