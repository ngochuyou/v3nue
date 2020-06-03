/**
 * 
 */
package v3nue.core.model.exceptions;

/**
 * @author Ngoc Huy
 *
 */
public class NoFactoryException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public NoFactoryException(String message) {
		super();
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMessage() {
		return message;
	}

}
