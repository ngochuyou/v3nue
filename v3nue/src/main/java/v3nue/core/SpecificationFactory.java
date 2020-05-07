package v3nue.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.Specification;
import v3nue.core.model.entity.specification.SpecificationFree;
import v3nue.core.utils.Constants;

@Component
@Order(value = 1)
public class SpecificationFactory implements ApplicationManager {

	@Autowired
	private SpecificationFree specificationFree;

	@Autowired
	@Qualifier("modelManager")
	private ModelManager entityManager;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, Specification<? extends AbstractEntity>> rulesMap = new HashMap<>();

	public void initialize() {
		// TODO Auto-generated constructor stub
		logger.info("Initializing SpecificationFactory");
		logger.info("Scanning for Specifications");
		// @formatter:off
		contextUtils.getComponentStream(Constants.specificationPackage,
				Stream.of(new AssignableTypeFilter(Specification.class),
						new AnnotationTypeFilter(EntitySpecification.class))
					.collect(Collectors.toSet()),
				null, false)
			.forEach(bean -> {
				try {
					Class<?> clazz = Class.forName(bean.getBeanClassName());
					EntitySpecification es = clazz.getDeclaredAnnotation(EntitySpecification.class);
					Component component =  clazz.getDeclaredAnnotation(Component.class);
					
					if (component == null) {
						throw new RuntimeException("Non-standard Specification, ensure to have a @Component on " + clazz.getName());
					}
					
					for (Class<? extends AbstractEntity> gene: es.entity()) {
						rulesMap.put(reflector.getEntityName(gene),
								(Specification<?>) context.getBean(reflector.getBeanName(clazz)));
						logger.info(clazz.getName() + " applied on " + gene.getName());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					SpringApplication.exit(context);
				}
			});

		entityManager.getEntityTree().forEach(branch -> {
			try {
				String clazzName = reflector.getEntityName(branch.getNode());
				Specification<?> compositeSpec = null;
				Specification<?> tempSpec = null;
				Stack<Specification<?>> stack = new Stack<>();

				tempSpec = rulesMap.get(reflector.getEntityName(branch.getNode()));
				
				if (tempSpec != null) {
					stack.add(tempSpec);
				}
				
				if (branch.getParent() != null) {
					tempSpec = rulesMap.get(reflector.getEntityName(branch.getParent().getNode()));
					
					if (tempSpec != null) {
						stack.add(tempSpec);
					}
				}
				
				if (!stack.isEmpty()) {
					while(!stack.isEmpty()) {
						logger.info("Applying " + stack.peek().getClass().getName() + " on " + branch.getNode().getName() + " after checking inheritance");
						compositeSpec = (compositeSpec == null ? stack.pop() : compositeSpec.and(stack.pop()));
					}
					
					rulesMap.put(clazzName, compositeSpec);
					
					return;
				}

				rulesMap.put(clazzName, specificationFree);
				logger.info("Applying " + SpecificationFree.class.getName() + " on " + clazzName + " after checking inheritance");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		logger.info("Finished scanning for Specifications");
		logger.info("Finished initializing SpecificationFactory");
		// @formatter:on
	}

	public Specification<?> getSpecification(String clazzName) {

		return rulesMap.get(clazzName);
	}

}
