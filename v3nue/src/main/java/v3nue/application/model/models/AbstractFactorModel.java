/**
 * 
 */
package v3nue.application.model.models;

import v3nue.core.model.AbstractFactor;
import v3nue.core.model.annotations.Relation;

/**
 * @author Ngoc Huy
 *
 */
@Relation(relation = AbstractFactor.class)
public class AbstractFactorModel extends AbstractModel {

	private String id;

	private String createdBy;

	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
