/**
 * 
 */
package v3nue.application.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

import v3nue.core.model.AbstractFactor;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "seatings")
public class Seating extends AbstractFactor {

	@Range(min = 0)
	@Column(nullable = false, columnDefinition = "MEDIUMINT")
	private int capatiy;

	private String size;

	public int getCapatiy() {
		return capatiy;
	}

	public void setCapatiy(int capatiy) {
		this.capatiy = capatiy;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
