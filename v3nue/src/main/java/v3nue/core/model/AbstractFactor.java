/**
 * 
 */
package v3nue.core.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

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
	@Size(min = 8, max = 255)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	protected String id;

	@Column(name = "created_by", nullable = false)
	protected String createdBy;

	@Size(min = 1, max = 255)
	@Column(nullable = false, unique = true)
	protected String name;

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
