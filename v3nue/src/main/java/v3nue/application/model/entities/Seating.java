/**
 * 
 */
package v3nue.application.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import v3nue.core.model.AbstractFactor;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "seatings")
public class Seating extends AbstractFactor {
	// Maximum Value UNSIGNED of MEDIUMINT by MySQL
	@Size(min = 0, max = 16777215)
	@Column(nullable = false, columnDefinition = "MEDIUMINT")
	private int capacity;

	private String size;

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
