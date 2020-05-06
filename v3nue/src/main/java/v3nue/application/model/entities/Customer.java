/**
 * 
 */
package v3nue.application.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Ngoc Huy
 *
 */
@Entity
public class Customer extends Account {

	@Column(name = "prestige_point")
	private int prestigePoint;

	public int getPrestigePoint() {
		return prestigePoint;
	}

	public void setPrestigePoint(int prestigePoint) {
		this.prestigePoint = prestigePoint;
	}

}
