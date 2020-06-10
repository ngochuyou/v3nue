/**
 * 
 */
package v3nue.application.service.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import v3nue.application.model.entities.Contract;
import v3nue.core.service.ApplicationService;

/**
 * @author Ngoc Huy
 *
 */
@Service
public class ContractService implements ApplicationService<Contract> {

	@Autowired
	private AbstractEntityService abstractService;

	@Override
	public <X extends Contract> X doMandatory(X instance) {
		// TODO Auto-generated method stub
		X x = abstractService.doMandatory(instance);
		float total = instance.getBooking().getVenue().getPrice();
		// @formatter:off
		instance.setSeatingsDetails(instance.getSeatingsDetails()
				.stream()
				.map(detail -> {
					detail.setContract(instance);
					
					return detail;
				}).collect(Collectors.toSet()));
		
		instance.setMandatoriesDetails(instance.getMandatoriesDetails()
				.stream()
				.map(detail -> {
					detail.setContract(instance);
					
					return detail;
				}).collect(Collectors.toSet()));
		
		instance.setFoodsAndDrinksDetails(instance.getFoodsAndDrinksDetails()
				.stream()
				.map(detail -> {
					detail.setContract(instance);
					
					return detail;
				}).collect(Collectors.toSet()));
		// @formatter:on
		total += (instance.getMandatoriesDetails().stream()
				.mapToDouble(detail -> detail.getAmount() * detail.getMandatory().getPrice()).sum()
				+ instance.getFoodsAndDrinksDetails().stream()
						.mapToDouble(detail -> detail.getAmount() * detail.getFoodsAndDrinks().getPrice()).sum());
		x.setTotalAmount(total);
		
		return x;
	}

}
