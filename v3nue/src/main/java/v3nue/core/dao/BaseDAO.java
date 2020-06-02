package v3nue.core.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import v3nue.core.SpecificationFactory;
import v3nue.core.model.AbstractEntity;
import v3nue.core.model.entity.specification.Specification;
import v3nue.core.service.ServiceResult;
import v3nue.core.utils.ClassReflector;

@Repository("baseDAO")
public class BaseDAO {

	@Autowired
	protected SessionFactory factory;

	@Autowired
	protected ClassReflector reflector;

	@Autowired
	protected SpecificationFactory specificationFactory;

	protected ServiceResult<?> result;

	protected final String OK = "OK.";

	protected final String INVALID_RESOURCE = "Invalid resource.";

	protected final String EXISTED_RESOURCE = "Invalid already exisited.";

	// readOnly transactions: BEGIN
	@Transactional(readOnly = true)
	public <T extends AbstractEntity> T findById(Object id, Class<T> clazz) {
		if (id == null || clazz == null) {
			return null;
		}

		return factory.getCurrentSession().get(clazz, (Serializable) id);
	}

	@Transactional(readOnly = true)
	public <T extends AbstractEntity> T findOne(CriteriaQuery<T> query) {
		Session session = factory.getCurrentSession();
		Query<T> hql = session.createQuery(query);

		return hql.getResultStream().findFirst().orElse(null);
	}

	@Transactional(readOnly = true)
	public <T extends AbstractEntity> List<T> findWithNativeQuery(String nativeQuery, Class<T> clazz,
			List<Object> values) {
		Session s = factory.getCurrentSession();
		Query<T> query = s.createNativeQuery(nativeQuery, clazz);

		for (int i = 0; i < values.size(); i++) {
			query.setParameter(i + 1, values.get(i));
		}

		return query.getResultList();
	}

	@Transactional(readOnly = true)
	public <T extends AbstractEntity> List<T> find(CriteriaQuery<T> query) {
		Session session = factory.getCurrentSession();
		Query<T> hql = session.createQuery(query);

		return hql.getResultList();
	}

	@Transactional(readOnly = true)
	public <T extends AbstractEntity> List<T> find(CriteriaQuery<T> query, int start, int quantity) {
		Session session = factory.getCurrentSession();
		Query<T> hql = session.createQuery(query);

		hql.setFirstResult(start);
		hql.setMaxResults(quantity);

		return hql.getResultList();
	}

	@Transactional(readOnly = true)
	public <T extends AbstractEntity> long count(CriteriaQuery<Long> query) {
		Session ss = factory.getCurrentSession();
		Query<Long> hql = ss.createQuery(query);

		return hql.getSingleResult();
	}

	// readOnly transactions: END
	// non-readOnly transactions: BEGIN

	/**
	 * @param object to insert.
	 * @param clazz  object's class.
	 * 
	 * @return the action result with the status code along with the object.
	 */
	@SuppressWarnings({ "unchecked" })
	public <T extends AbstractEntity> ServiceResult<T> insert(T object, Class<T> clazz) {
		Session ss = factory.getCurrentSession();
		// check the parameters
		if (object == null || clazz == null) {
			return ServiceResult.error(object, new HashMap<>(Map.of("id", INVALID_RESOURCE)), 400);
		}
		// check if the entity's Id exsits
		T instance = this.findById(object.getId(), clazz);

		if (instance != null) {
			return ServiceResult.error(instance, new HashMap<>(Map.of("id", EXISTED_RESOURCE)), 409);
		}
		// validate entity
		Specification<T> specification = (Specification<T>) specificationFactory
				.getSpecification(reflector.getEntityName(clazz));

		if (!(result = specification.isSatisfiedBy(object)).isOkay()) {
			return ServiceResult.error(object, result.getMessages(), result.getStatus());
		}

		ss.evict(object);
		ss.save(object);

		return ServiceResult.success(object, result.getMessages());
	}

	/**
	 * @param object updated object
	 * @param clazz  type of object
	 * 
	 * @return the object if found otherwise null
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity> ServiceResult<T> update(T object, Class<T> clazz) {
		Session ss = factory.getCurrentSession();
		// check the parameters
		if (object == null || clazz == null) {
			return ServiceResult.error(object, new HashMap<>(Map.of("id", INVALID_RESOURCE)), 400);
		}
		// check if entity exists
		T instance = this.findById(object.getId(), clazz);

		if (instance == null) {
			return ServiceResult.error(instance, new HashMap<>(Map.of("id", EXISTED_RESOURCE)), 409);
		}
		// validate entity and save
		Specification<T> specification = (Specification<T>) specificationFactory
				.getSpecification(reflector.getEntityName(clazz));

		if (!(result = specification.isSatisfiedBy(object)).isOkay()) {
			return ServiceResult.error(object, result.getMessages(), result.getStatus());
		}

		ss.detach(instance);
		ss.update(object);

		return ServiceResult.success(object, result.getMessages());
	}

	/**
	 * @param object removed object
	 * @param clazz  type of object
	 * 
	 * @return the <code>DatabaseOperationResult</code>
	 */
	public <T extends AbstractEntity> ServiceResult<T> remove(Object id, Class<T> clazz) {
		Session ss = factory.getCurrentSession();
		// check the parameters
		if (id == null || clazz == null) {
			return ServiceResult.error(null, new HashMap<>(Map.of("id", INVALID_RESOURCE)), 400);
		}
		// check if entity exists
		T instance = this.findById(id, clazz);

		if (instance == null) {
			return ServiceResult.error(instance, new HashMap<>(Map.of("id", EXISTED_RESOURCE)), 409);
		}

		instance.setActive(false);
		ss.update(instance);

		return ServiceResult.success(instance, new HashMap<>(Map.of("id", "Document removed")));
	}

}
