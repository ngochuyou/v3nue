package v3nue.core.utils;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		context = applicationContext;
	}

	public static ApplicationContext getContext() {

		return context;
	}

	public Stream<BeanDefinition> getComponentStream(String basePackage, Set<TypeFilter> includeFilters,
			Set<TypeFilter> excludeFilters, boolean useDefaultFilters) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
				useDefaultFilters);

		if (includeFilters != null) {
			includeFilters.forEach(scanner::addIncludeFilter);
		}

		if (excludeFilters != null) {
			excludeFilters.forEach(scanner::addExcludeFilter);
		}

		return scanner.findCandidateComponents(basePackage).stream();
	}

}
