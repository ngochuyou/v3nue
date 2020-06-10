/**
 * 
 */
package v3nue.application.model.models;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import v3nue.application.model.entities.Booking;
import v3nue.application.model.entities.Contract;
import v3nue.application.model.entities.ContractFoodsAndDrinksDetail;
import v3nue.application.model.entities.ContractMandatoryDetail;
import v3nue.application.model.entities.ContractSeatingDetail;
import v3nue.core.model.annotations.Relation;

/**
 * @author Ngoc Huy
 *
 */
@Relation(relation = Contract.class)
public class ContractModel extends AbstractFactorModel {

	@Min(0)
	private float agreedAmount;

	@Min(0)
	private float depositAmount;

	@Min(0)
	private float totalAmount;

	@NotBlank
	private String description;

	@NotNull
	private AccountModel supervisor;

	@NotNull
	private Booking booking;

	private List<ContractSeatingDetail> seatingsDetails;

	private List<ContractMandatoryDetail> mandatoriesDetails;

	private List<ContractFoodsAndDrinksDetail> foodsAndDrinksDetails;

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

	public AccountModel getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(AccountModel supervisor) {
		this.supervisor = supervisor;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public List<ContractSeatingDetail> getSeatingsDetails() {
		return seatingsDetails;
	}

	public void setSeatingsDetails(List<ContractSeatingDetail> seatingsDetails) {
		this.seatingsDetails = seatingsDetails;
	}

	public List<ContractMandatoryDetail> getMandatoriesDetails() {
		return mandatoriesDetails;
	}

	public void setMandatoriesDetails(List<ContractMandatoryDetail> mandatoriesDetails) {
		this.mandatoriesDetails = mandatoriesDetails;
	}

	public List<ContractFoodsAndDrinksDetail> getFoodsAndDrinksDetails() {
		return foodsAndDrinksDetails;
	}

	public void setFoodsAndDrinksDetails(List<ContractFoodsAndDrinksDetail> foodsAndDrinksDetails) {
		this.foodsAndDrinksDetails = foodsAndDrinksDetails;
	}

}
