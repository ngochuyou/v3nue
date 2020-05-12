package v3nue.core.model.factory;

import org.springframework.stereotype.Component;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;

@Component("defaultEMFactory")
public class DefaultEMFactory implements EMFactory<AbstractEntity, Model> {

	@Override
	public AbstractEntity produce(Model model) {
		return (AbstractEntity) model;
	}

	@Override
	public Model produce(AbstractEntity entity) {
		// TODO Auto-generated method stub
		return entity;
	}

}
