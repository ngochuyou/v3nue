/**
 * 
 */
package v3nue.application;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import v3nue.core.ApplicationManager;
import v3nue.core.model.AbstractFactor;
import v3nue.core.utils.Constants;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Order(value = 10)
public class FactorManager implements ApplicationManager {

	private Map<String, Class<? extends AbstractFactor>> factorMap;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		logger.info("Initializing FactorManager");
		this.factorMap = new HashMap<>();
		contextUtils.getComponentStream(Constants.entityPackage,
				Stream.of(new AssignableTypeFilter(AbstractFactor.class)).collect(Collectors.toSet()), null, false)
				.forEach(bean -> {
					try {
						Class<? extends AbstractFactor> clazz = (Class<? extends AbstractFactor>) Class
								.forName(bean.getBeanClassName());

						factorMap.put(clazz.getSimpleName().toLowerCase(), clazz);
						logger.info("Put " + clazz.getSimpleName().toLowerCase() + " as key for getting "
								+ clazz.getName() + " factor");
					} catch (Exception e) {
						e.printStackTrace();
						SpringApplication.exit(context);
					}
				});
		logger.info("Finished initializing FactorManager");
	}

	public Class<? extends AbstractFactor> forName(String name) {
		return this.factorMap.get(name);	
	}
}
