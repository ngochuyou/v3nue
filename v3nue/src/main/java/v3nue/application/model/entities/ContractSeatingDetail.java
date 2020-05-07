/**
 * 
 */
package v3nue.application.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.Size;

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
	private Contract contract;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("seatingId")
	private Seating seating;

	@Size(min = 0)
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

}
