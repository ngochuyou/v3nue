/**
 * 
 */
package v3nue.application.model.models;

import v3nue.application.model.entities.Customer;
import v3nue.core.model.annotations.Relation;

/**
 * @author Ngoc Huy
 *
 */
@Relation(relation = Customer.class)
public class CustomerModel extends AccountModel {

	private int prestigePoint;

	public int getPrestigePoint() {
		return prestigePoint;
	}

	public void setPrestigePoint(int prestigePoint) {
		this.prestigePoint = prestigePoint;
	}

}
