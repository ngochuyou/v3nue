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

import org.hibernate.validator.constraints.Range;

import v3nue.core.model.AbstractEntity;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "contracts_foods_and_drinks_details")
public class ContractFoodsAndDrinksDetail extends AbstractEntity {

	@EmbeddedId
	private ContractFoodsAndDrinksDetailId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("contractId")
	private Contract contract;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("foodsAndDrinksId")
	private FoodsAndDrinks foodsAndDrinks;

	@Range(min = 0)
	@Column(nullable = false)
	private int amount;

	@Range(min = 0)
	@Column(nullable = false)
	private float total;

	public ContractFoodsAndDrinksDetailId getId() {
		return id;
	}

	public void setId(ContractFoodsAndDrinksDetailId id) {
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

}

@Embeddable
class ContractFoodsAndDrinksDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "contract_id")
	private String contractId;

	@Column(name = "foods_and_drinks_id")
	private String foodsAndDrinksId;

	public ContractFoodsAndDrinksDetailId(String contractId, String foodsAndDrinksId) {
		super();
		this.contractId = contractId;
		this.foodsAndDrinksId = foodsAndDrinksId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getFoodsAndDrinksId() {
		return foodsAndDrinksId;
	}

	public void setFoodsAndDrinksId(String foodsAndDrinksId) {
		this.foodsAndDrinksId = foodsAndDrinksId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}