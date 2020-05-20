package v3nue.core.model.entity.specification;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import v3nue.core.dao.DatabaseOperationResult;
import v3nue.core.model.AbstractEntity;

@Component
public class SpecificationFree extends CompositeSpecification<AbstractEntity> {

	@Override
	public DatabaseOperationResult<v3nue.core.model.AbstractEntity> isSatisfiedBy(AbstractEntity entity) {
		// TODO Auto-generated method stub
		return new DatabaseOperationResult<>(entity, new HashMap<>(), 200);
	}

}
