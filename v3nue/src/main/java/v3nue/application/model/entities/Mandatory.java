/**
 * 
 */
package v3nue.application.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

import v3nue.core.model.AbstractFactor;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "mandatories")
public class Mandatory extends AbstractFactor {

	@Range(min = 0)
	@Column(nullable = false)
	private float price;

	@ManyToOne(optional = false)
	private MandatoryType type;
	// @formatter:off
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "mandatories_suppliers",
		joinColumns = @JoinColumn(name = "mandatory_id"),
		inverseJoinColumns = @JoinColumn(name = "supplier_id"))
	private Set<Supplier> suppliers;
	// @formatter:on
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public MandatoryType getType() {
		return type;
	}

	public void setType(MandatoryType type) {
		this.type = type;
	}

	public Set<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(Set<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

}
