/**
 * 
 */
package v3nue.application.model.models;

import java.util.Date;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;
import v3nue.core.model.annotations.Relation;

/**
 * @author Ngoc Huy
 *
 */
@Relation(relation = AbstractEntity.class)
public class AbstractModel implements Model {

	private Object id;

	private Date createdDate;

	private Date updatedDate;

	private boolean isActive;

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

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

}
