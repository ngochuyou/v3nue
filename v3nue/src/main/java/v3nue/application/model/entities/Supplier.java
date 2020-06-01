/**
 * 
 */
package v3nue.application.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;

import v3nue.core.model.AbstractFactor;

/**
 * @author Ngoc Huy
 *
 */
@Entity
@Table(name = "suppliers")
public class Supplier extends AbstractFactor {

	@Digits(fraction = 0, integer = 15)
	@Column(nullable = false)
	private String phone;

	@Email
	@Column(nullable = true)
	private String email;

	private String specialization;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

}
