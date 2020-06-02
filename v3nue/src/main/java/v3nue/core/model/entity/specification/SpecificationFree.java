package v3nue.core.model.entity.specification;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import v3nue.core.model.AbstractEntity;
import v3nue.core.service.ServiceResult;

@Component
public class SpecificationFree extends CompositeSpecification<AbstractEntity> {

	@Override
	public ServiceResult<v3nue.core.model.AbstractEntity> isSatisfiedBy(AbstractEntity entity) {
		// TODO Auto-generated method stub
		return new ServiceResult<>(entity, new HashMap<>(), 200);
	}

}
