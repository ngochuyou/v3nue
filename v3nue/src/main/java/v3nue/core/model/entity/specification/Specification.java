/**
 * 
 */
package v3nue.core.model.entity.specification;

import v3nue.core.model.AbstractEntity;
import v3nue.core.service.ServiceResult;

/**
 * Codewise of every entities's validation in the application. Classes of this
 * type will be called by DAOs before every interactions with the database.
 * 
 * @param T Generic type of the entity
 * 
 * @author Ngoc Huy
 *
 */
public interface Specification<T extends AbstractEntity> {
	
	/**
	 * Validate the entity
	 * 
	 * @param entity the instance to validate
	 * 
	 * @return The object which represents the validating result.
	 */
	ServiceResult<T> isSatisfiedBy(T entity);

	/**
	 * A function to support the validating process of inherited entities.
	 * <p>
	 * Specification objects of this type will be queued, first in first called.
	 * Thus, usages of this function should ensure the proper inheritance.
	 * </p>
	 * 
	 * @param next
	 * @return
	 */
	Specification<?> and(Specification<?> next);
	
	/**
	 * Status codes 
	 */
	final int OK = 200;
	
	final int BAD = 400;
	
	final int CONFLICT = 409;
	
}
