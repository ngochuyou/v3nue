/**
 * 
 */
package v3nue.application.model.models;

import v3nue.application.model.entities.Personnel;
import v3nue.application.model.entities.Specialization;
import v3nue.core.model.annotations.Relation;

/**
 * @author Ngoc Huy
 *
 */
@Relation(relation = Personnel.class)
public class PersonnelModel extends AccountModel {

	private Specialization specialization;

	public Specialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

}
