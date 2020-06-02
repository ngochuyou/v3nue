package v3nue.core.model.factory;

import org.springframework.stereotype.Component;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;

@Component("defaultEMFactory")
@SuppressWarnings("unchecked")
public class DefaultEMFactory implements EMFactory<AbstractEntity, Model> {

	@Override
	public <X extends AbstractEntity> X produceEntity(Model model, Class<X> clazz) {
		return (X) model;
	}

	@Override
	public <X extends Model> X produceModel(AbstractEntity entity, Class<X> clazz) {
		// TODO Auto-generated method stub
		return (X) entity;
	}

}
