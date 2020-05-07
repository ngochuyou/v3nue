package v3nue.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Component
public class ClassReflector {
	// @formatter:off
	private final List<Class<? extends Annotation>> commonBeanTypes =
			Arrays.asList(Component.class, Service.class,
					Controller.class, Repository.class);
	// @formatter:on
	public String getEntityName(Class<?> clazz) {
		if (clazz == null)
			return null;

		Entity anno = clazz.getDeclaredAnnotation(Entity.class);

		if (anno != null) {
			String name = anno.name();

			return StringUtils.isEmpty(name) ? clazz.getSimpleName() : name;
		}

		return clazz.getSimpleName();
	}

	public String getComponentName(Class<?> clazz) {
		if (clazz == null)
			return null;

		Component anno = clazz.getDeclaredAnnotation(Component.class);

		if (anno != null) {
			String name = anno.value();

			if (StringUtils.isEmpty(name)) {
				name = clazz.getSimpleName();

				char firstLetter = Character.toLowerCase(name.charAt(0));

				name = firstLetter + name.substring(1);
			}

			return name;
		}

		return null;
	}

	public String getBeanName(Class<?> clazz) {
		try {
			for (Annotation a : clazz.getAnnotations()) {
				if (this.commonBeanTypes.contains(a.annotationType())) {
					Method m = a.getClass().getDeclaredMethod("value");
					String value = m.invoke(a).toString();

					if (value.length() == 0) {
						return clazz.getSimpleName().toLowerCase().charAt(0) + clazz.getSimpleName().substring(1);
					}

					return value;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return null;
	}

	public boolean doesImplement(Class<?> target, Class<?> superz) {
		Set<Type> types = new HashSet<>();

		while (target != null) {
			if (target.getInterfaces() != null) {
				// @formatter:off
				types.addAll(Stream.of(target.getInterfaces())
						.collect(Collectors.toSet()));
				// @formatter:on
			}

			target = target.getSuperclass();
		}

		return types.contains(superz);
	}

	public boolean isExtendedFrom(Class<?> target, Class<?> superz) {
		Class<?> superClazz = target;

		while (superClazz != null) {
			if (superClazz == superz) {
				return true;
			}

			superClazz = superClazz.getSuperclass();
		}

		return false;
	}

	public Stack<Class<?>> getClassStack(Class<?> clazz) {
		Class<?> superClazz = clazz;
		Stack<Class<?>> stack = new Stack<>();

		while (superClazz != null) {
			stack.push(superClazz);
			superClazz = superClazz.getSuperclass();
		}

		return stack;
	}

	public Field getAttribute(String attrName, Class<?> clazz) {
		Field f;

		for (Class<?> c : getClassStack(clazz)) {
			try {
				if ((f = c.getDeclaredField(attrName)) != null)
					return f;
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				continue;
			}
		}

		return null;
	}
}
