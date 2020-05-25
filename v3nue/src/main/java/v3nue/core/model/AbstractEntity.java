/**
 * 
 */
package v3nue.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An abstract class extended from Model to identify a Model as an Entity
 * 
 * @author Ngoc Huy
 *
 */
@MappedSuperclass
public abstract class AbstractEntity implements Model {

	@CreationTimestamp
	@Column(name = "created_date", nullable = false)
	protected Date createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date", nullable = false)
	protected Date updatedDate;

	@JsonProperty
	@Column(name = "is_active", nullable = false)
	protected boolean isActive;

	/**
	 * This method is used to make sure every entities in the application has a
	 * field named id and use it as a Primary Key
	 * 
	 * @return the entity Primary Key
	 */
	public abstract Object getId();

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
