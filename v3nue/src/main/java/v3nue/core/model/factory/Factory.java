package v3nue.core.model.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import v3nue.core.model.AbstractEntity;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Factory {

	public Class<? extends AbstractEntity> entity();

}