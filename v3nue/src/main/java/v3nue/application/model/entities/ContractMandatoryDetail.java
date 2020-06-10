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
@Table(name = "contracts_mandatories_details")
public class ContractMandatoryDetail extends AbstractEntity {

	@EmbeddedId
	private ContractMandatoryDetailId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("contractId")
	@JsonIgnore
	private Contract contract;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("mandatoryId")
	@JsonIgnore
	private Mandatory mandatory;

	@Min(0)
	@Column(nullable = false)
	private int amount;

	@Min(0)
	@Column(nullable = false)
	private float total;

	public ContractMandatoryDetailId getId() {
		return id;
	}

	public void setId(ContractMandatoryDetailId id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Mandatory getMandatory() {
		return mandatory;
	}

	public void setMandatory(Mandatory mandatory) {
		this.mandatory = mandatory;
	}

}

@Embeddable
class ContractMandatoryDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "contract_id")
	private String contractId;

	@Column(name = "mandatory_id")
	private String mandatoryId;

	public ContractMandatoryDetailId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContractMandatoryDetailId(String contractId, String mandatoryId) {
		super();
		this.contractId = contractId;
		this.mandatoryId = mandatoryId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getMandatoryId() {
		return mandatoryId;
	}

	public void setMandatoryId(String mandatoryId) {
		this.mandatoryId = mandatoryId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this == obj)
			return true;

		if (!(obj instanceof ContractMandatoryDetailId))
			return false;

		ContractMandatoryDetailId that = (ContractMandatoryDetailId) obj;

		return Objects.equals(this.contractId, that.getContractId())
				&& Objects.equals(this.mandatoryId, that.getMandatoryId());
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(this.contractId, this.mandatoryId);
	}

}