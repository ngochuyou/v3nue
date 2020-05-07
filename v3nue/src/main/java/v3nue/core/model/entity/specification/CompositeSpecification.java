/**
 * 
 */
package v3nue.core.model.entity.specification;

import v3nue.core.model.AbstractEntity;

/**
 * Specifications of this type are able to 'chain' it self with other
 * specification to form a 'validation chain'. However, usages need to ensure
 * the targeted entity must have the same Generic type otherwise some exceptions
 * may occur.
 * 
 * @author Ngoc Huy
 *
 */
public abstract class CompositeSpecification<T extends AbstractEntity> implements Specification<T> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Specification<?> and(Specification<?> next) {
		// TODO Auto-generated method stub
		return new And(this, next);
	}

}

class And<T extends AbstractEntity> extends CompositeSpecification<T> {

	private Specification<T> left;

	private Specification<T> right;

	protected And(Specification<T> left, Specification<T> right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public EntityValidationResult<T> isSatisfiedBy(T entity) {
		// TODO Auto-generated method stub
		EntityValidationResult<T> leftResult = this.left.isSatisfiedBy(entity);
		EntityValidationResult<T> rightResult = this.right.isSatisfiedBy(entity);

		leftResult.getMessages().putAll(rightResult.getMessages());

		return new EntityValidationResult<T>(entity, leftResult.getMessages(),
				leftResult.isOkay() && rightResult.isOkay() ? 200 : 401);
	}

	public Specification<T> getLeft() {
		return left;
	}

	public void setLeft(Specification<T> left) {
		this.left = left;
	}

	public Specification<T> getRight() {
		return right;
	}

	public void setRight(Specification<T> right) {
		this.right = right;
	}

}