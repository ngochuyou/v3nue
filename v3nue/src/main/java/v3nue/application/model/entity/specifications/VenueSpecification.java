/**
 * 
 */
package v3nue.application.model.entity.specifications;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Venue;
import v3nue.core.model.annotations.EntitySpecification;
import v3nue.core.model.entity.specification.CompositeSpecification;
import v3nue.core.service.ServiceResult;
import v3nue.core.utils.StringUtil;

/**
 * @author Ngoc Huy
 *
 */
@Component
@EntitySpecification(entity = { Venue.class })
public class VenueSpecification extends CompositeSpecification<Venue> {

	@Override
	public ServiceResult<Venue> isSatisfiedBy(Venue entity) {
		// TODO Auto-generated method stub
		Map<String, String> messages = new HashMap<String, String>();
		int status = OK;
		int capacity = entity.getCapacity();

		if (capacity < 0 || capacity > 16777215) {
			messages.put("capacity", "capacity must be between 0 and 16777215.");
			status = BAD;
		}
		
		if (StringUtil.isEmpty(entity.getLocation())) {
			messages.put("location", "Location information can not be empty.");
			status = BAD;
		}
		
		if (entity.getPrice() <= 0) {
			messages.put("price", "price amount can not be negative.");
			status = BAD;
		}

		return new ServiceResult<Venue>(entity, messages, status);
	}

}
