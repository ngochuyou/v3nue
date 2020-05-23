/**
 * 
 */
package v3nue.application.service.services;

import org.springframework.stereotype.Service;

import v3nue.core.model.AbstractEntity;
import v3nue.core.service.ApplicationService;

/**
 * @author Ngoc Huy
 *
 */
@Service
public class AbstractEntityService implements ApplicationService<AbstractEntity> {

	@Override
	public <X extends AbstractEntity> X doMandatory(X instance) {
		// TODO Auto-generated method stub
		if (instance == null) {
			return null;
		}
		
		instance.setActive(true);

		return instance;
	}
	
}
