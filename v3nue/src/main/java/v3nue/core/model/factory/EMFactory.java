package v3nue.core.model.factory;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;

public interface EMFactory<T extends AbstractEntity, M extends Model> {

	<X extends T> X produceEntity(M model, Class<X> clazz);

	<X extends M> X produceModel(T entity, Class<X> clazz);

}