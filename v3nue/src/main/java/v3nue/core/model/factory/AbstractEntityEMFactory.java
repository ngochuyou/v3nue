/**
 * 
 */
package v3nue.core.model.factory;

import org.springframework.stereotype.Component;

import v3nue.application.model.models.AbstractModel;
import v3nue.core.model.AbstractEntity;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Factory(entity = AbstractEntity.class)
public class AbstractEntityEMFactory implements EMFactoryForInheritedModels<AbstractEntity, AbstractModel> {

	private AbstractEMConverter converter = new AbstractEMConverter();

	@Override
	public AbstractEntity produce(AbstractModel model) {
		// TODO Auto-generated method stub
		AbstractEntity entity = new AbstractEntity() {
			@Override
			public Object getId() {
				// TODO Auto-generated method stub
				return model.getId();
			}
		};

		entity.setActive(model.isActive());
		entity.setCreatedDate(model.getCreatedDate());
		entity.setUpdatedDate(model.getUpdatedDate());

		return entity;
	}

	@Override
	public AbstractModel produce(AbstractEntity entity) {
		// TODO Auto-generated method stub
		AbstractModel model = new AbstractModel() {
			@Override
			public Object getId() {
				// TODO Auto-generated method stub
				return entity.getId();
			}
		};

		model.setActive(entity.isActive());
		model.setCreatedDate(entity.getCreatedDate());
		model.setUpdatedDate(entity.getUpdatedDate());

		return model;
	}

	@Override
	public <X extends AbstractEntity> X convert(AbstractEntity instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return converter.convert(instance, clazz);
	}

	@Override
	public <X extends AbstractModel> X convert(AbstractModel instance, Class<X> clazz) {
		// TODO Auto-generated method stub
		return converter.convert(instance, clazz);
	}

}
