/**
 * 
 */
package v3nue.application.model.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

import v3nue.core.model.AbstractEntity;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "contract_seatings_details")
public class ContractSeatingDetail extends AbstractEntity {

	@EmbeddedId
	private ContractSeatingDetailId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("contractId")
	@JsonIgnore
	private Contract contract;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("seatingId")
	@JsonIgnore
	private Seating seating;

	@Min(0)
	@Column(nullable = false)
	private int amount;

	public ContractSeatingDetailId getId() {
		return id;
	}

	public void setId(ContractSeatingDetailId id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Seating getSeating() {
		return seating;
	}

	public void setSeating(Seating seating) {
		this.seating = seating;
	}

}

@Embeddable
class ContractSeatingDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "contract_id")
	private String contractId;

	@Column(name = "contract_id")
	private String seatingId;

	public ContractSeatingDetailId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContractSeatingDetailId(String contractId, String seatingId) {
		super();
		this.contractId = contractId;
		this.seatingId = seatingId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getSeatingId() {
		return seatingId;
	}

	public void setSeatingId(String seatingId) {
		this.seatingId = seatingId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this == obj)
			return true;

		if (!(obj instanceof ContractSeatingDetailId))
			return false;

		ContractSeatingDetailId that = (ContractSeatingDetailId) obj;

		return Objects.equals(this.contractId, that.getContractId())
				&& Objects.equals(this.seatingId, that.getSeatingId());
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(this.contractId, this.seatingId);
	}

}
