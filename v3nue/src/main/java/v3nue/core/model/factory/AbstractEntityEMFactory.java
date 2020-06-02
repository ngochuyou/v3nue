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
public class AbstractEntityEMFactory implements EMFactory<AbstractEntity, AbstractModel> {

	private AbstractEMConverter converter = new AbstractEMConverter();

	@Override
	public <X extends AbstractEntity> X produceEntity(AbstractModel model, Class<X> clazz) {
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

		return converter.convert(entity, clazz);
	}

	@Override
	public <X extends AbstractModel> X produceModel(AbstractEntity entity, Class<X> clazz) {
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

		return converter.convert(model, clazz);
	}

}
