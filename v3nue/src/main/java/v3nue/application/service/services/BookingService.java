/**
 * 
 */
package v3nue.application.service.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import v3nue.application.model.entities.Booking;
import v3nue.core.service.ApplicationService;

/**
 * @author Ngoc Huy
 *
 */
@Service
public class BookingService implements ApplicationService<Booking> {

	@Override
	public <X extends Booking> X doMandatory(X instance) {
		// TODO Auto-generated method stub
		instance.setActive(instance.getExpiryDate().after(new Date()));
		
		return instance;
	}

}
