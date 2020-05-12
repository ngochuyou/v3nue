package v3nue.core.model.factory;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;

public interface EMFactory<T extends AbstractEntity, M extends Model> {

	public abstract T produce(M model);

	public abstract M produce(T entity);

}