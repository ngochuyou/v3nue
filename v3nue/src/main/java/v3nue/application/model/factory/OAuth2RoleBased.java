/**
 * 
 */
package v3nue.application.model.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import v3nue.core.utils.AccountRole;

/**
 * @author Ngoc Huy
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface OAuth2RoleBased {
	
	/**
	 * Indecates the targeted {@code AccountRole}. If none is provided, the default
	 * target will be {@code AccountRole.ADMIN}
	 * 
	 * @return the {@code AccountRole}
	 */
	public AccountRole role() default AccountRole.Admin;
	
}
