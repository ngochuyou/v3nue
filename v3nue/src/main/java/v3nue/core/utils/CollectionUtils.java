package v3nue.core.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class CollectionUtils {

	public static <S> Set<S> toSet(Collection<S> collection) {

		return (collection != null ? collection.stream().map(ele -> ele).collect(Collectors.toSet()) : new HashSet<>());
	}

	public static <S> List<S> toList(Collection<S> collection) {

		return (collection != null ? collection.stream().map(ele -> ele).collect(Collectors.toList())
				: Arrays.asList());
	}

}
