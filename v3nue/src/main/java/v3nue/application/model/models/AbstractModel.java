/**
 * 
 */
package v3nue.application.model.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;
import v3nue.core.model.annotations.Relation;

/**
 * @author Ngoc Huy
 *
 */
@Relation(relation = AbstractEntity.class)
public abstract class AbstractModel implements Model {

	protected Date createdDate;

	protected Date updatedDate;

	@JsonProperty
	protected boolean isActive;

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
