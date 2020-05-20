/**
 * 
 */
package v3nue.core.dao;

import java.util.Map;

import v3nue.core.model.AbstractEntity;

/**
 * Objects of this type represent the result of validating process.
 * 
 * <p>
 * <b>200</b> for a success validation, otherwise a failure.
 * </p>
 * <p>
 * Messages about the violations will be held in a
 * <code><b>{@literal Map<String, String>}</b></code> with key set represents
 * the violated fields and value set represents the messages.
 * </p>
 * 
 * @author Ngoc Huy
 *
 */
public class DatabaseOperationResult<T> {

	private T entity;

	private Map<String, String> messages;

	private int status;

	public DatabaseOperationResult(T entity, Map<String, String> messages, int status) {
		super();
		this.entity = entity;
		this.messages = messages;
		this.status = status;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Map<String, String> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isOkay() {

		return this.status == 200;
	}

	public static <T extends AbstractEntity> DatabaseOperationResult<T> success(T entity,
			Map<String, String> messages) {

		return new DatabaseOperationResult<T>(entity, messages, 200);
	}

	public static <T extends AbstractEntity> DatabaseOperationResult<T> error(T entity,
			Map<String, String> messages, int status) {

		return new DatabaseOperationResult<T>(entity, messages, status);
	}

}
