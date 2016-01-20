/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.isotrol.impe3.core.modules;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.filter;
import static net.sf.derquinsej.Methods.withParameters;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.PortalConfiguration;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.Path;
import com.isotrol.impe3.api.modules.SpringSimple;
import com.isotrol.impe3.core.component.ComponentDefinition;
import com.isotrol.impe3.core.component.ComponentException;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.config.ConfigurationException;
import com.isotrol.impe3.core.config.PortalConfigurationDefinition;
import com.isotrol.impe3.core.support.TypeRelated;


/**
 * Loader for modules.
 * @author Andres Rodriguez.
 * 
 * @param <T> Module type.
 */
final class ModuleDefinitionLoader<T extends Module> extends TypeRelated<T> {
	private static final String SLASH = "/";
	private static final String DEFAULT_PATH = "impe3-module.xml";
	/** Predicate that identifies dependencies. */
	private static final Predicate<Method> IS_DEPENDENCY = withParameters(1);
	/** Predicate that identifies provisions. */
	private static final Predicate<Method> IS_PROVISION = withParameters(0);
	/** Predicate that identifies invalid methods. */
	private static final Predicate<Method> IS_INVALID = new Predicate<Method>() {
		public boolean apply(Method input) {
			return input.getParameterTypes().length > 1;
		}
	};
	private static final ImmutableSet<Class<Component>> FORBIDDEN_DEPENDENCIES = ImmutableSet.of(Component.class);
	private static final ImmutableSet<Class<URIGenerator>> FORBIDDEN_CNN_DEPENDENCIES = ImmutableSet
		.of(URIGenerator.class);

	private DefaultListableBeanFactory registry;
	private Map<String, Provision> provisions;
	private ModuleType moduleType = null;
	private ConfigurationDefinition<?> configuration = null;
	private Dependency configurationDependency = null;
	private PortalConfigurationDefinition<?> portalConfiguration = null;
	private Dependency portalConfigurationDependency = null;
	private Map<String, Dependency> dependencies;
	private Set<String> actions;

	/**
	 * Loads a module definition.
	 * @param module Module class to analyze.
	 * @throws ModuleException if the module is invalid.
	 */
	static <T extends Module> ModuleDefinitionLoader<T> load(Class<T> module) throws ModuleException {
		checkNotNull(module, "A module class must be provided");
		if (!module.isInterface()) {
			throw new NonInterfaceModuleException(module);
		}
		return new ModuleDefinitionLoader<T>(module);
	}

	private ModuleDefinitionLoader(Class<T> module) throws ModuleException {
		super(module);
		checkMethods();
		if (module.isAnnotationPresent(SpringSimple.class)) {
			new SpringSimpleLoader();
		} else {
			new SpringXMLLoader();
		}
	}

	ModuleType getModuleType() {
		return moduleType;
	}

	Map<String, Provision> getProvisions() {
		return provisions;
	}

	Map<String, Dependency> getDependencies() {
		return dependencies;
	}

	DefaultListableBeanFactory getRegistry() {
		return registry;
	}

	ConfigurationDefinition<?> getConfiguration() {
		return configuration;
	}

	Dependency getConfigurationDependency() {
		return configurationDependency;
	}
	
	/**
	 * @return the portalConfiguration
	 */
	public PortalConfigurationDefinition<?> getPortalConfiguration() {
		return portalConfiguration;
	}

	/**
	 * @return the portalConfigurationDependency
	 */
	public Dependency getPortalConfigurationDependency() {
		return portalConfigurationDependency;
	}

	Set<String> getActions() {
		return actions;
	}

	private void checkMethods() throws ModuleException {
		final List<Method> methods = getMethods();
		// The interface must not be empty
		if (methods.isEmpty()) {
			throw new EmptyModuleException(getType());
		}
		// And all methods must be either dependencies, provisions or a configuration.
		if (!Iterables.isEmpty(filter(methods, IS_INVALID))) {
			throw new InvalidMethodsException(getType());
		}
	}

	private boolean loadConfiguration(Class<?> klass) throws ModuleException {
		if (Configuration.class.isAssignableFrom(klass)) {
			// is a configuration
			if (configuration != null) {
				throw new DuplicateModuleConfigurationException(getType());
			}
			try {
				configuration = ConfigurationDefinition.of(klass.asSubclass(Configuration.class));
			} catch (ConfigurationException e) {
				throw new ModuleConfigurationException(getType(), e);
			}
			return true;
		}
		return false;
	}
	
	private boolean loadPortalConfiguration(Class<?> klass) throws ModuleException {
		if (PortalConfiguration.class.isAssignableFrom(klass)) {
			// is a configuration
			if (portalConfiguration != null) {
				throw new DuplicateModuleConfigurationException(getType());
			}
			try {
				portalConfiguration = PortalConfigurationDefinition.of(klass.asSubclass(PortalConfiguration.class));
			} catch (ConfigurationException e) {
				throw new ModuleConfigurationException(getType(), e);
			}
			return true;
		}
		return false;
	}

	private void provisionsLoaded() throws ModuleException {
		if (checkNotNull(provisions, "Provisions not loaded").isEmpty()) {
			throw new NoProvisionsException(getType());
		}
	}

	private void setModuleType(ModuleType type) throws ModuleException {
		checkNotNull(type);
		if (moduleType == null) {
			moduleType = type;
		} else if (moduleType != type) {
			throw new ProvidesBothException(getType());
		}
	}

	/**
	 * A provision is an action if its type is a concrete class that does not implement Component.
	 * @param type Type to test.
	 * @return True if the type is avalid action type.
	 */
	private boolean isActionType(final Class<?> type) {
		if (type.isInterface() || type.isPrimitive() || Component.class.isAssignableFrom(type)) {
			return false;
		}
		return true;
	}

	private void addDependency(Method m, boolean required) throws ModuleException {
		final String bean = m.getName();
		if (provisions.containsKey(bean)) {
			throw new InvalidDependsException(getType(), bean);
		}
		final Class<?>[] types = m.getParameterTypes();
		if (types.length != 1 || !Void.TYPE.equals(m.getReturnType())) {
			throw new InvalidDependsException(getType(), bean);
		}
		final Class<?> klass = types[0];
		final boolean config = loadConfiguration(klass);
		if (!config) {
			if (!Modules.isInternalDependency(klass) && !klass.isInterface()) {
				throw new NonInterfaceDependsException(getType(), bean, klass);
			}
			if (dependencies.containsKey(bean)) {
				throw new DuplicateDependsException(getType(), bean);
			}
			for (final Class<?> fd : FORBIDDEN_DEPENDENCIES) {
				if (fd.isAssignableFrom(klass)) {
					throw new ForbiddenDependsException(getType(), bean, klass);
				}
			}
			if (moduleType == ModuleType.CONNECTOR) {
				for (final Class<?> fd : FORBIDDEN_CNN_DEPENDENCIES) {
					if (fd.isAssignableFrom(klass)) {
						throw new ForbiddenDependsException(getType(), bean, klass);
					}
				}
			}
		}
		final Dependency d = new Dependency(bean, klass, m, required);
		dependencies.put(bean, d);
		if (config) {
			configurationDependency = d;
		}
	}

	private final class SpringXMLLoader {
		private String path;

		SpringXMLLoader() throws ModuleException {
			// Path
			loadPath();
			// Bean registry
			loadRegistry();
			// Provisions
			loadProvisions();
			// Dependencies
			loadDependencies();
		}

		private void loadPath() throws ModuleException {
			final Path pathAnnotation = getType().getAnnotation(Path.class);
			if (pathAnnotation == null) {
				path = DEFAULT_PATH;
			} else {
				path = pathAnnotation.value();
				if (!StringUtils.hasText(path)) {
					throw new EmptyPathException(getType());
				}
				if (path.contains(SLASH)) {
					throw new NonLocalPathException(getType(), path);
				}
			}
		}

		private void loadRegistry() throws ModuleException {
			final ClassPathResource resource = new ClassPathResource(path, getType());
			registry = new DefaultListableBeanFactory();
			try {
				final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
				reader.loadBeanDefinitions(resource);
			} catch (BeanDefinitionStoreException e) {
				throw new DefinitionModuleException(getType(), path, e);
			}
		}

		private void loadProvisions() throws ModuleException {
			final Iterable<Method> provides = filter(getMethods(), IS_PROVISION);
			provisions = Maps.newHashMap();
			actions = Sets.newHashSet();
			for (Method m : provides) {
				if (isAction(m)) {
					if (moduleType == ModuleType.CONNECTOR) {
						throw new ProvidesBothException(getType());
					}
					actions.add(m.getName());
					continue;
				}
				final Provision p = getProvision(m);
				setModuleType(p.getModuleType());
				provisions.put(p.getBeanName(), p);
			}
			provisionsLoaded();
		}

		/**
		 * A provision method is an action if the return type is object.
		 * @param m Method to test.
		 * @return True if the method is an action.
		 */
		private boolean isAction(Method m) {
			if (!isActionType(m.getReturnType())) {
				return false;
			}
			final String bean = m.getName();
			if (!registry.containsBean(bean)) {
				throw new ActionNotFoundException(getType(), bean);
			}
			if (!BeanDefinition.SCOPE_PROTOTYPE.equals(registry.getBeanDefinition(bean).getScope())) {
				throw new InvalidScopeException(getType(), bean);
			}
			return true;
		}

		private Provision getProvision(Method m) throws ModuleException {
			final String bean = m.getName();
			if (!registry.containsBean(bean)) {
				throw new ProvisionNotFoundException(getType(), bean);
			}
			final Class<?> klass = m.getReturnType();
			if (Modules.isForbiddenProvision(klass)) {
				throw new ForbiddenProvisionException(getType(), bean);
			}
			final Provision p;
			final boolean component = Component.class.isAssignableFrom(klass);
			final BeanDefinition bd = registry.getBeanDefinition(bean);
			if (component) {
				try {
					p = new ComponentProvision(m, ComponentDefinition.of(klass.asSubclass(Component.class)));
					if (!bd.isPrototype()) {
						throw new InvalidScopeException(getType(), bean);
					}
				} catch (ComponentException e) {
					throw new InvalidComponentException(getType(), bean, e);
				}
			} else if (!component && !klass.isInterface()) {
				throw new NonInterfaceProvidesException(getType(), bean, klass);
			} else {
				p = new ConnectorProvision(bean, klass, m);
				if (!bd.isSingleton()) {
					throw new InvalidScopeException(getType(), bean);
				}
			}
			if (!klass.isAssignableFrom(registry.getType(bean))) {
				throw new InvalidProvisionTypeException(getType(), bean, klass);
			}
			return p;
		}

		private void loadDependencies() throws ModuleException {
			final Iterable<Method> dependMethods = filter(getMethods(), IS_DEPENDENCY);
			dependencies = Maps.newHashMap();
			for (Method m : dependMethods) {
				final String bean = m.getName();
				final Class<?>[] types = m.getParameterTypes();
				if (types.length != 1 || !Void.TYPE.equals(m.getReturnType())) {
					throw new InvalidDependsException(getType(), bean);
				}
				final Class<?> klass = types[0];
				final boolean required = !registry.containsBean(bean);
				final boolean config = loadConfiguration(klass);
				final boolean portalConfig = loadPortalConfiguration(klass);
				
				if (!config) {
					if (!Modules.isInternalDependency(klass) && !klass.isInterface()) {
						throw new NonInterfaceDependsException(getType(), bean, klass);
					}
					if (dependencies.containsKey(bean)) {
						throw new DuplicateDependsException(getType(), bean);
					}
					for (final Class<?> fd : FORBIDDEN_DEPENDENCIES) {
						if (fd.isAssignableFrom(klass)) {
							throw new ForbiddenDependsException(getType(), bean, klass);
						}
					}
					if (moduleType == ModuleType.CONNECTOR) {
						for (final Class<?> fd : FORBIDDEN_CNN_DEPENDENCIES) {
							if (fd.isAssignableFrom(klass)) {
								throw new ForbiddenDependsException(getType(), bean, klass);
							}
						}
					}
				}
				final Dependency d = new Dependency(bean, klass, m, required);
				dependencies.put(bean, d);
				if (config) {
					configurationDependency = d;
				}
				if (portalConfig) {
					portalConfigurationDependency = d;
				}
			}
		}
	}

	private final class SpringSimpleLoader {
		SpringSimpleLoader() throws ModuleException {
			// Bean registry
			loadRegistry();
			// Provisions
			loadProvisions();
			// Dependencies
			loadDependencies();
		}

		private void loadRegistry() throws ModuleException {
			registry = new DefaultListableBeanFactory();
		}

		/**
		 * Creates a bean definition.
		 * @param type Bean type.
		 * @param scope Bean scope.
		 * @return A bean definition.
		 */
		private BeanDefinition create(Class<?> type, String scope) {
			checkNotNull(type);
			checkNotNull(scope);
			final GenericBeanDefinition definition = new GenericBeanDefinition();
			definition.setBeanClass(type);
			definition.setScope(scope);
			definition.validate();
			return definition;
		}

		private void loadProvisions() throws ModuleException {
			final Iterable<Method> provides = filter(getMethods(), IS_PROVISION);
			provisions = Maps.newHashMap();
			actions = Sets.newHashSet();
			for (Method m : provides) {
				final Class<?> provisionType = m.getReturnType();
				if (isActionType(provisionType)) {
					actions.add(m.getName());
				} else {
					final Provision p = getProvision(m);
					provisions.put(p.getBeanName(), p);
				}
				registry.registerBeanDefinition(m.getName(), create(provisionType, BeanDefinition.SCOPE_PROTOTYPE));
				setModuleType(ModuleType.COMPONENT);
			}
			provisionsLoaded();
			registry.registerBeanDefinition(UUID.randomUUID().toString(),
				create(AutowiredAnnotationBeanPostProcessor.class, BeanDefinition.SCOPE_SINGLETON));
			registry.registerBeanDefinition(UUID.randomUUID().toString(),
				create(CommonAnnotationBeanPostProcessor.class, BeanDefinition.SCOPE_SINGLETON));
		}

		private Provision getProvision(Method m) throws ModuleException {
			final String bean = m.getName();
			final Class<?> klass = m.getReturnType();
			if (Modules.isForbiddenProvision(klass)) {
				throw new ForbiddenProvisionException(getType(), bean);
			}
			final boolean component = Component.class.isAssignableFrom(klass);
			if (component) {
				try {
					return new ComponentProvision(m, ComponentDefinition.of(klass.asSubclass(Component.class)));
				} catch (ComponentException e) {
					throw new InvalidComponentException(getType(), bean, e);
				}
			}
			throw new InvalidProvisionTypeException(getType(), bean, klass);
		}

		private void loadDependencies() throws ModuleException {
			final Iterable<Method> dependMethods = filter(getMethods(), IS_DEPENDENCY);
			dependencies = Maps.newHashMap();
			for (Method m : dependMethods) {
				addDependency(m, true);
			}
		}
	}

}
