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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import v3nue.application.model.factory.EMFactoryManagerProvider;
import v3nue.application.model.factory.OAuth2RoleBased;
import v3nue.core.model.factory.EMFactoryManager;
import v3nue.core.security.server.authorization.CustomUserDetails;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.Constants;

/**
 * @author Ngoc Huy
 *
 */
@Order(value = 7)
@Component("oauth2AuthenticationBasedEMFactoryManagerProvider")
public class OAuth2AuthenticationBasedEMFactoryManagerProvider implements EMFactoryManagerProvider {

	private Map<AccountRole, EMFactoryManager> managerMap;

	private SecurityContext securityContext = SecurityContextHolder.getContext();

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		this.managerMap = new HashMap<AccountRole, EMFactoryManager>();
		contextUtils.getComponentStream(Constants.factoryPackage,
				Stream.of(new AssignableTypeFilter(EMFactoryManager.class)).collect(Collectors.toSet()), null, false)
				.forEach(bean -> {
					try {
						Class<? extends EMFactoryManager> clazz = (Class<? extends EMFactoryManager>) Class
								.forName(bean.getBeanClassName());
						OAuth2RoleBased anno = clazz.getDeclaredAnnotation(OAuth2RoleBased.class);

						if (anno == null) {
							throw new Exception("OAuth2RoleBased annotation not found on " + clazz.getName());
						}

						managerMap.put(anno.role(),
								(EMFactoryManager) context.getBean(reflector.getComponentName(clazz)));
						logger.info("Assigning " + clazz.getName() + " for Model managing on OAuth2 " + anno.role()
								+ " authentiation.");
					} catch (Exception e) {
						e.printStackTrace();
						SpringApplication.exit(context);
					}
				});
	}

	@Override
	public EMFactoryManager getEMFactoryManager() {
		// TODO Auto-generated method stub
		return managerMap.get(getAuthenticationRole());
	}

	public EMFactoryManager getEMFactoryManager(AccountRole role) {
		// TODO Auto-generated method stub
		return managerMap.get(role);
	}

	public AccountRole getAuthenticationRole() {
		Authentication authentication = securityContext.getAuthentication();

		if (authentication == null) {
			return AccountRole.Anonymous;
		}

		return ((CustomUserDetails) authentication).getRole();
	}

}
