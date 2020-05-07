package v3nue.core.model.exceptions;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;

/**
 * This exception indicates any wrong usage of {@link Model} in this application
 * 
 * @author Ngoc Huy
 *
 */
public class NonStandardModelException extends Exception {
	/* 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public NonStandardModelException(String message) {
		this.message = message;
	}

	public NonStandardModelException(Class<? extends Model> modelClazz, Class<? extends AbstractEntity> entityClazz) {
		this.message = "Entity " + entityClazz.getSimpleName() + " was modelized into a Model"
				+ " but one or more fields in " + modelClazz.getSimpleName()
				+ " used the Entity type, please use the specified Model type instead.";
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return this.message;
	}

}