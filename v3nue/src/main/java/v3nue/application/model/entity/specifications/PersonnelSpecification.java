/**
 * 
 */
package v3nue.application.model.entity.specifications;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Personnel;
import v3nue.application.model.entities.Specialization;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecificationWithDAO;
import v3nue.core.service.ServiceResult;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { Personnel.class })
public class PersonnelSpecification extends CompositeSpecificationWithDAO<Personnel> {

	@Override
	public ServiceResult<Personnel> isSatisfiedBy(Personnel entity) {
		// TODO Auto-generated method stub
		Specialization specialization = entity.getSpecialization();
		Map<String, String> messages = new HashMap<String, String>();

		if (specialization == null || dao.findById(specialization.getId(), Specialization.class) == null) {
			messages.put("specialization",
					"Personnel's specialization can not be null and must be a valid information.");

			return new ServiceResult<Personnel>(entity, messages, BAD);
		}

		return new ServiceResult<Personnel>(entity, messages, OK);
	}

}
