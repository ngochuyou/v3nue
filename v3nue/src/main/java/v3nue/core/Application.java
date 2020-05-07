/**
 * 
 */
package v3nue.core;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import v3nue.core.utils.ApplicationContextUtils;
import v3nue.core.utils.ClassReflector;
import v3nue.core.utils.Constants;

/**
 * @author Ngoc Huy
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = { Constants.basePackage })
@EnableAutoConfiguration(exclude = { HibernateJpaAutoConfiguration.class })
public class Application {

	@Autowired
	private ApplicationContextUtils contextUtils;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ClassReflector reflector;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	@SuppressWarnings("unchecked")
	public void doAfterStartUp() {
		// @formatter:off
		contextUtils.getComponentStream(Constants.basePackage,
				Stream.of(new AssignableTypeFilter(ApplicationManager.class),
							new AnnotationTypeFilter(Order.class))
					.collect(Collectors.toSet()),	
				null, false)
			.map(bean -> {
				try {
					Class<? extends ApplicationManager> managerClass = (Class<? extends ApplicationManager>) Class.forName(bean.getBeanClassName());

					return managerClass;
				} catch (Exception e) {
					return null;
				}
			})
			.filter(clazz -> clazz != null)
			.sorted((left, right) -> {
				Order leftAnno = left.getDeclaredAnnotation(Order.class);
				Order rightAnno = right.getDeclaredAnnotation(Order.class);

				return leftAnno.value() - rightAnno.value();
			})
			.forEach(managerClass -> {
				ApplicationManager manager = (ApplicationManager)
						context.getBean(reflector.getComponentName(managerClass));

				manager.initialize();
			});
		// @formatter:on
	}

}
