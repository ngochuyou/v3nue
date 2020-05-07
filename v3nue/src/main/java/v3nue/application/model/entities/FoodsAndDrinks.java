/**
 * 
 */
package v3nue.application.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import v3nue.core.model.AbstractFactor;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "foods_and_drinks")
public class FoodsAndDrinks extends AbstractFactor {

	@Size(min = 0)
	@Column(nullable = false)
	private float price;

	@Column(nullable = false)
	private String photo;

	@ManyToOne(optional = false)
	private FoodsAndDrinksType type;

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public FoodsAndDrinksType getType() {
		return type;
	}

	public void setType(FoodsAndDrinksType type) {
		this.type = type;
	}

}
