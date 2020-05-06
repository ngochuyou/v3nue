/**
 * 
 */
package v3nue.core.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

/**
 * Every class which extends from this class is determined as a 'factor'. One
 * will be controlled by application admins/operators
 * 
 * @author Ngoc Huy
 *
 */
@MappedSuperclass
public class AbstractFactor extends AbstractEntity {

	@Id
	@Range(min = 8, max = 255)
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column(name = "created_by", nullable = false)
	private String createdBy;

	@Range(min = 1, max = 255)
	@Column(nullable = false, unique = true)
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
