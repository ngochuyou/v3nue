/**
 * 
 */
package v3nue.application.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import v3nue.core.model.AbstractFactor;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "venues")
public class Venue extends AbstractFactor {
	// Maximum Value UNSIGNED of MEDIUMINT by MySQL
	@Max(value = 16777215)
	@Min(value = 0)
	@Column(nullable = false, columnDefinition = "MEDIUMINT")
	private int capacity;

	private String size;

	@Column(nullable = false)
	private String location;

	@Min(value = 0)
	@Column(nullable = false)
	private float price;

	@Column(columnDefinition = "TEXT")
	private String description;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
