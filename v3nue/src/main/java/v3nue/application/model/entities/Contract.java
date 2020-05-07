/**
 * 
 */
package v3nue.application.model.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import v3nue.core.model.AbstractFactor;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "contracts")
public class Contract extends AbstractFactor {

	@Size(min = 0)
	@Column(nullable = false)
	private float agreedAmount;

	@Size(min = 0)
	@Column(nullable = false)
	private float depositAmount;

	@Size(min = 0)
	@Column(nullable = false)
	private float totalAmount;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@ManyToOne(optional = false)
	private Personnel supervisor;

	@OneToOne(optional = false)
	private Booking booking;

	@OneToMany(mappedBy = "contract", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<ContractSeatingDetail> seatingsDetails;

	@OneToMany(mappedBy = "contract", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<ContractMandatoryDetail> mandatoriesDetails;

	@OneToMany(mappedBy = "contract", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<ContractFoodsAndDrinksDetail> foodsAndDrinksDetails;

	public float getAgreedAmount() {
		return agreedAmount;
	}

	public void setAgreedAmount(float agreedAmount) {
		this.agreedAmount = agreedAmount;
	}

	public float getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(float depositAmount) {
		this.depositAmount = depositAmount;
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Personnel getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Personnel supervisor) {
		this.supervisor = supervisor;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public Set<ContractSeatingDetail> getSeatingsDetails() {
		return seatingsDetails;
	}

	public void setSeatingsDetails(Set<ContractSeatingDetail> seatingsDetails) {
		this.seatingsDetails = seatingsDetails;
	}

	public Set<ContractMandatoryDetail> getMandatoriesDetails() {
		return mandatoriesDetails;
	}

	public void setMandatoriesDetails(Set<ContractMandatoryDetail> mandatoriesDetails) {
		this.mandatoriesDetails = mandatoriesDetails;
	}

	public Set<ContractFoodsAndDrinksDetail> getFoodsAndDrinksDetails() {
		return foodsAndDrinksDetails;
	}

	public void setFoodsAndDrinksDetails(Set<ContractFoodsAndDrinksDetail> foodsAndDrinksDetails) {
		this.foodsAndDrinksDetails = foodsAndDrinksDetails;
	}

}
