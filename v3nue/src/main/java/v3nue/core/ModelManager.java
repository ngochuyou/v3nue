package v3nue.core;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import v3nue.core.model.AbstractEntity;
import v3nue.core.model.EntityInheritanceTree;
import v3nue.core.model.Model;
import v3nue.core.model.annotations.Relation;
import v3nue.core.model.exceptions.NonStandardModelException;
import v3nue.core.utils.Constants;

@Component("modelManager")
@SuppressWarnings("unchecked")
@Order(value = 0)
public class ModelManager implements ApplicationManager {

	private Set<Class<? extends Model>> modelSet = new HashSet<>();

	private EntityInheritanceTree entityTree = new EntityInheritanceTree();

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<Class<? extends AbstractEntity>, Class<? extends Model>> relationMap = new HashMap<>();

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		logger.info("Initializing ModelManager");
		logger.info("Adding Entities to EntityTree");
		// @formatter:off
		contextUtils.getComponentStream(Constants.entityPackage,
				Stream.of(new AssignableTypeFilter(AbstractEntity.class),
						new AnnotationTypeFilter(Entity.class))
				.collect(Collectors.toSet()),
				null, false)
					.forEach(bean -> {
						try {
							Class<? extends AbstractEntity> clazz = (Class<? extends AbstractEntity>) Class.forName(bean.getBeanClassName());
							
							entityTree.addTree(clazz);
							logger.info(bean.getBeanClassName() + " has bean added to EntityTree");
							relationMap.put(clazz, clazz);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							SpringApplication.exit(context);
						}
					});
		logger.info("Finished adding Entities to EntityTree");
		logger.info("Adding Models to ModelSet");
		
		Set<Class<? extends AbstractEntity>> modelizedEntityClasses = new HashSet<>();

		contextUtils.getComponentStream(Constants.modelPackage,
				Stream.of(new AssignableTypeFilter(Model.class)).collect(Collectors.toSet()),
				Stream.of(new AssignableTypeFilter(AbstractEntity.class)).collect(Collectors.toSet()),
				false)
					.forEach(bean -> {
						try {
							Class<? extends Model> modelClass = (Class<? extends Model>) Class.forName(bean.getBeanClassName());
							Relation anno = modelClass.getDeclaredAnnotation(Relation.class);
							
							if (anno == null) {
								throw new NonStandardModelException("Relation annotaion not found on " + bean.getBeanClassName());
							}

							logger.info(bean.getBeanClassName() + " has bean added to ModelSet");
							relationMap.put(anno.relation(), modelClass);
							modelSet.add(modelClass);
							modelizedEntityClasses.add(anno.relation());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							SpringApplication.exit(context);
						}
					});
		logger.info("Finished adding Models to ModelSet");
		logger.info("Checking Model usage");
		modelSet.forEach(modelClass -> {
			try {
				Field[] fields = modelClass.getDeclaredFields();
				
				for (Field f : fields) {
					Class<?> attributeClass = f.getType();
					
					if (reflector.isExtendedFrom(attributeClass, AbstractEntity.class)) {
						if (modelizedEntityClasses.contains((Class<? extends AbstractEntity>) attributeClass)) {
							throw new NonStandardModelException(modelClass, (Class<? extends AbstractEntity>) attributeClass);
						}

						continue;
					}
					
					if (reflector.doesImplement(attributeClass, Collection.class)) {
						Class<? extends AbstractEntity> cla = (Class<? extends AbstractEntity>) Class.forName(f.getGenericType().getTypeName().replaceAll(".+<|>", ""));
						
						if (reflector.isExtendedFrom(cla, AbstractEntity.class)) {
							if (modelizedEntityClasses.contains(cla)) {
								throw new NonStandardModelException(modelClass, (Class<? extends AbstractEntity>) cla);
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SpringApplication.exit(context);
			}
		});
		logger.info("Finished checking Model usage");
		// @formatter:on
		logger.info("Finished initializing ModelManager");
	}

	public EntityInheritanceTree getEntityTree() {
		return entityTree;
	}

	public <T extends Model> Class<T> forModelClass(Class<? extends AbstractEntity> clazz) {

		return (Class<T>) relationMap.get(clazz);
	}
}
