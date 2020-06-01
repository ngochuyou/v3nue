/**
 * 
 */
package v3nue.application.model.models;

import java.util.List;

import v3nue.application.model.entities.Mandatory;
import v3nue.application.model.entities.MandatoryType;
import v3nue.application.model.entities.Supplier;
import v3nue.core.model.annotations.Relation;

/**
 * @author Ngoc Huy
 *
 */
@Relation(relation = Mandatory.class)
public class MandatoryModel extends AbstractFactorModel {

	private float price;

	private MandatoryType type;

	private List<Supplier> suppliers;

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

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

}
