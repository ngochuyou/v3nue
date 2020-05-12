package v3nue.core.model.factory;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.Model;

public interface EMFactoryForInheritedModels<T extends AbstractEntity, M extends Model> extends EMFactory<T, M> {

	public abstract <X extends T> X convert(T object, Class<X> clazz);

	public abstract <X extends M> X convert(M object, Class<X> clazz);

}
