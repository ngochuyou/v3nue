/**
 * 
 */
package v3nue.application.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import v3nue.core.model.AbstractFactor;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "authorities")
public class Authority extends AbstractFactor {

	public Authority() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Authority(@Size(min = 8, max = 255) String id, String createdBy, @Size(min = 1, max = 255) String name,
			boolean isActive) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.createdBy = createdBy;
		this.name = name;
		this.isActive = isActive;
	}

}
