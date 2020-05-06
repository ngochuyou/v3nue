/**
 * 
 */
package v3nue.application.model.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Ngoc Huy
 *
 */
@Entity
public class Personnel extends Account {

	@ManyToOne
	private Specialization specialization;

	public Specialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

}
