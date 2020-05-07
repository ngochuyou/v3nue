/**
 * 
 */
package v3nue.core.model.entity.specification;

import org.hibernate.SessionFactory;

import v3nue.core.dao.BaseDAO;
import v3nue.core.model.AbstractEntity;
import v3nue.core.utils.ApplicationContextUtils;

/**
 * Utility abstract class to hold the {@link BaseDAO} bean. Any Specification
 * which queries the database should be extending this class
 * 
 * <p>
 * Side notes, usages should consider the Hibernate Session FlushMode
 * </p>
 * 
 * @author Ngoc Huy
 * @see CompositeSpecification
 */
public abstract class CompositeSpecificationWithDAO<T extends AbstractEntity> extends CompositeSpecification<T> {

	protected final BaseDAO dao = ApplicationContextUtils.getContext().getBean(BaseDAO.class);

	protected final SessionFactory sessionFactory = ApplicationContextUtils.getContext().getBean(SessionFactory.class);

}
