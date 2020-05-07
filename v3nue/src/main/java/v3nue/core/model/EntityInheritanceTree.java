package v3nue.core.model;

import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import v3nue.core.utils.ClassReflector;

public class EntityInheritanceTree implements Comparable<EntityInheritanceTree> {

	private Class<? extends AbstractEntity> node = AbstractEntity.class;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private EntityInheritanceTree parent;

	private Set<EntityInheritanceTree> childs = new TreeSet<>();

	private ClassReflector reflector = new ClassReflector();

	public EntityInheritanceTree() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EntityInheritanceTree(Class<? extends AbstractEntity> node, EntityInheritanceTree parent) {
		super();
		this.node = node;
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	public void addTree(Class<? extends AbstractEntity> clazz) {
		Stack<Class<?>> stack = reflector.getClassStack(clazz);

		while (stack.peek() != AbstractEntity.class) {
			stack.pop();
		}

		while (!stack.isEmpty()) {
			this.add((Class<? extends AbstractEntity>) stack.pop());
		}
	}

	public void add(Class<? extends AbstractEntity> clazz) {
		if (clazz == null || this.node == clazz) {
			return;
		}

		if (this.node == clazz.getSuperclass()) {
			if (this.find(clazz) != null) {
				return;
			}

			logger.debug("Adding " + clazz.getSimpleName() + " to " + this.node.getSimpleName());
			this.childs.add(new EntityInheritanceTree(clazz, this));
		}

		this.childs.forEach(c -> c.add(clazz));
	}

	public EntityInheritanceTree find(Class<?> clazz) {
		if (this.node == clazz)
			return this;

		EntityInheritanceTree target = null;

		for (EntityInheritanceTree child : this.childs) {
			if ((target = child.find(clazz)) != null)
				return target;
		}

		return target;
	}

	public void visit() {
		if (this.node == null) {
			return;
		}

		logger.info("node : " + this.node.getSimpleName()
				+ (this.parent == null ? " is the highest root " : " with root: " + this.parent.node.getSimpleName()));

		this.childs.forEach(c -> c.visit());
	}

	public void forEach(Consumer<EntityInheritanceTree> consumer) {
		consumer.accept(this);

		this.childs.forEach(tree -> tree.forEach(consumer));
	}

	@Override
	public int compareTo(EntityInheritanceTree o) {
		// TODO Auto-generated method stub
		if (o == null)
			return 0;

		return o.node.getSimpleName().compareTo(this.node.getSimpleName());
	}

	public Class<? extends AbstractEntity> getNode() {
		return node;
	}

	public EntityInheritanceTree getParent() {
		return parent;
	}

}
