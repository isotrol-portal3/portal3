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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Principal;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.ActionContext;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.config.PortalConfigurationDefinition;
import com.isotrol.impe3.core.support.Definition;
import com.isotrol.impe3.core.support.SingleValueSupport;


/**
 * Object that encapsulates the definition of a module.
 * @author Andres Rodriguez.
 * @param <T> Module class.
 */
public abstract class ModuleDefinition<T extends Module> extends Definition<T> {
	/** Cache. */
	private static final SingleValueSupport<Class<?>, Object> CACHE = SingleValueSupport.create();
	/** Cast to connector provision. */
	private static final Function<Provision, ConnectorProvision> TO_CONNECTOR_PROVISION = new Function<Provision, ConnectorProvision>() {
		public ConnectorProvision apply(Provision from) {
			return ConnectorProvision.class.cast(from);
		}
	};
	/** Cast to component provision. */
	private static final Function<Provision, ComponentProvision> TO_COMPONENT_PROVISION = new Function<Provision, ComponentProvision>() {
		public ComponentProvision apply(Provision from) {
			return ComponentProvision.class.cast(from);
		}
	};

	/**
	 * Gets a valid module definition.
	 * @param module Module class to analyze.
	 * @return A valid module definition.
	 * @throws ModuleException if the module is invalid.
	 */
	public static <T extends Module> ModuleDefinition<T> of(Class<T> module) throws ModuleException {
		Preconditions.checkNotNull(module, "A module class must be provided");
		@SuppressWarnings("unchecked")
		final ModuleDefinition<T> d1 = (ModuleDefinition<T>) CACHE.get(module);
		if (d1 != null) {
			return d1;
		}
		@SuppressWarnings("unchecked")
		final ModuleDefinition<T> d2 = (ModuleDefinition<T>) CACHE.put(module,
			new Valid<T>(ModuleDefinitionLoader.load(module)));
		return d2;
	}

	/**
	 * Gets a valid module definition.
	 * @param name Module class name.
	 * @return A valid module definition.
	 * @throws ModuleException if the module is invalid.
	 * @throws IllegalArgumentException if the name is not a module class.
	 */
	public static ModuleDefinition<?> of(String name) throws ModuleException {
		Preconditions.checkNotNull(name, "A module class name must be provided");
		try {
			final Class<?> klass = Class.forName(name);
			final Class<? extends Module> type = klass.asSubclass(Module.class);
			return of(type);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Returns a possibly invalid module definition.
	 * @param module Module class to analyze.
	 * @return The module definition which may be invalid.
	 */
	public static <T extends Module> ModuleDefinition<T> getSafe(Class<T> module) {
		try {
			return of(module);
		} catch (ModuleException e) {
			return new Invalid<T>(module, e);
		}
	}

	private ModuleDefinition(Class<T> moduleClass) {
		super(moduleClass);
	}

	/**
	 * Returns the type of the module.
	 * @return The type of the module.
	 */
	public abstract ModuleType getModuleType();

	public ModuleException getError() {
		return null;
	}

	public ImmutableMap<String, Provision> getProvisions() {
		return ImmutableMap.of();
	}

	/**
	 * Returns whether the module exports only one provision.
	 * @return True if the module exports only one provision.
	 */
	public final boolean isSimple() {
		return getProvisions().size() == 1;
	}

	/**
	 * If is a connector module, return connector provision. Else, throws an illegalstate exception.
	 * @return map of connector provision.
	 */
	public final Map<String, ConnectorProvision> getConnectorProvisions() {
		Preconditions.checkState(getModuleType() == ModuleType.CONNECTOR);
		return Maps.transformValues(getProvisions(), TO_CONNECTOR_PROVISION);
	}

	/**
	 * If is a component module, return component provision. Else, throws an illegalstate exception.
	 * @return map of component provision.
	 */
	public final Map<String, ComponentProvision> getComponentProvisions() {
		Preconditions.checkState(getModuleType() == ModuleType.COMPONENT);
		return Maps.transformValues(getProvisions(), TO_COMPONENT_PROVISION);
	}

	public ImmutableMap<String, Dependency> getDependencies() {
		return ImmutableMap.of();
	}

	public Map<String, Dependency> getExternalDependencies() {
		return ImmutableMap.of();
	}

	public Map<String, Dependency> getInternalDependencies() {
		return ImmutableMap.of();
	}

	public ConfigurationDefinition<?> getConfiguration() {
		return null;
	}
	
	public PortalConfigurationDefinition<?> getPortalConfiguration() {
		return null;
	}

	public boolean isConfigurationRequired() {
		return false;
	}

	public boolean isPortalConfigurationDependencyRequired() {
		return false;
	}
	public boolean isConfigurationDependencyRequired() {
		return false;
	}

	public String getPortalConfigurationBeanName() {
		return null;
	}
	
	public String getConfigurationBeanName() {
		return null;
	}

	/**
	 * Return the set of actions defined by the module. Always empty for connector modules.
	 * @return An unmodifiable set of action names.
	 */
	public Set<String> getActions() {
		return ImmutableSet.of();
	}

	/**
	 * Return true if module required external deps.
	 * @return true if module required external deps.
	 */
	public final boolean hasRequiredExternalDependencies() {
		return Iterables.any(getExternalDependencies().values(), Dependency.IS_REQUIRED);
	}

	/**
	 * Return the required external dependencies.
	 * @return The required external dependencies.
	 */
	public final Map<String, Dependency> getRequiredExternalDependencies() {
		return Maps.filterValues(getExternalDependencies(), Dependency.IS_REQUIRED);
	}

	/**
	 * Returns a starter object for this module definition.
	 * @return A module starter.
	 * @throws UnsupportedOperationException If the module is invalid.
	 */
	public ModuleStarter<T> starter() {
		throw new UnsupportedOperationException();
	}

	private static final class Invalid<T extends Module> extends ModuleDefinition<T> {
		private final ModuleException error;

		private Invalid(Class<T> moduleClass, ModuleException error) {
			super(moduleClass);
			this.error = error;
		}

		@Override
		public ModuleType getModuleType() {
			return ModuleType.INVALID;
		}

		@Override
		public ModuleException getError() {
			return error;
		}
	}

	/**
	 * Valid.
	 * @author Andres Rodriguez.
	 */
	private static final class Valid<T extends Module> extends ModuleDefinition<T> {
		private final ModuleType moduleType;
		private final ImmutableMap<String, Provision> provisions;
		private final ImmutableMap<String, Dependency> dependencies;
		private final ConfigurationDefinition<?> configuration;
		private final Dependency configurationDependency;
		private final PortalConfigurationDefinition<?> portalConfiguration;
		private final Dependency portalConfigurationDependency;
		private final Set<String> actions;
		private final DefaultListableBeanFactory registry;

		private Valid(ModuleDefinitionLoader<T> loader) {
			super(loader.getType());
			this.moduleType = loader.getModuleType();
			this.provisions = ImmutableMap.copyOf(loader.getProvisions());
			this.dependencies = ImmutableMap.copyOf(loader.getDependencies());
			this.configuration = loader.getConfiguration();
			this.configurationDependency = loader.getConfigurationDependency();
			this.portalConfiguration = loader.getPortalConfiguration();
			this.portalConfigurationDependency = loader.getPortalConfigurationDependency();
			this.actions = ImmutableSet.copyOf(loader.getActions());
			this.registry = loader.getRegistry();
		}

		@Override
		public ModuleType getModuleType() {
			return moduleType;
		}

		public ImmutableMap<String, Provision> getProvisions() {
			return provisions;
		}

		public ImmutableMap<String, Dependency> getDependencies() {
			return dependencies;
		}

		@Override
		public Map<String, Dependency> getExternalDependencies() {
			return Maps.filterValues(dependencies, Dependency.IS_EXTERNAL);
		}

		@Override
		public Map<String, Dependency> getInternalDependencies() {
			return Maps.filterValues(dependencies, Dependency.IS_INTERNAL);
		}

		/**
		 * @return the portalConfiguration
		 */
		@Override
		public PortalConfigurationDefinition<?> getPortalConfiguration() {
			return portalConfiguration;
		}

		@Override
		public ConfigurationDefinition<?> getConfiguration() {
			return configuration;
		}

		@Override
		public boolean isConfigurationRequired() {
			if (configuration == null) {
				return false;
			}
			return configuration.isRequired() && configuration.hasMBPItems();
		}

		@Override
		public boolean isPortalConfigurationDependencyRequired() {
			if (portalConfigurationDependency == null) {
				return false;
			}
			return portalConfigurationDependency.isRequired() && isConfigurationRequired();
		}
		
		@Override
		public boolean isConfigurationDependencyRequired() {
			if (configurationDependency == null) {
				return false;
			}
			return configurationDependency.isRequired() && isConfigurationRequired();
		}

		@Override
		public String getPortalConfigurationBeanName() {
			Preconditions.checkState(portalConfigurationDependency != null);
			return portalConfigurationDependency.getBeanName();
		}
		
		@Override
		public String getConfigurationBeanName() {
			Preconditions.checkState(configurationDependency != null);
			return configurationDependency.getBeanName();
		}

		@Override
		public Set<String> getActions() {
			return actions;
		}

		@Override
		public ModuleStarter<T> starter() {
			return new Starter();
		}

		private final class Starter implements ModuleStarter<T> {
			private final Map<String, Object> supplied = Maps.newHashMap();

			private Starter() {
			}

			/**
			 * @see com.isotrol.impe3.core.modules.ModuleStarter#put(java.lang.String, java.lang.Object)
			 */
			public ModuleStarter<T> put(String name, Object value) {
				checkNotNull(name, "The dependency name for module [%s] must be provided", getTypeName());
				checkArgument(dependencies.containsKey(name), "[%s] is not a dependency for module [%s]", name,
					getTypeName());
				checkNotNull(value, "The value for dependency [%s] of module [%s] must be provided", name,
					getTypeName());
				final Dependency d = dependencies.get(name);
				checkArgument(d.getType().isInstance(value),
					"Expected type for dependency [%s] of module [%s]: [%s]. Provided: [%s]", name, getTypeName(), d
						.getType().getName(), value.getClass().getName());
				supplied.put(name, value);
				return this;
			}

			/**
			 * @see com.isotrol.impe3.core.modules.ModuleStarter#set(java.lang.Class, java.lang.Object)
			 */
			public ModuleStarter<T> set(Class<?> type, Object value) {
				checkNotNull(type, "The internal dependency name for module [%s] must be provided", getTypeName());
				checkNotNull(value, "The internal dependency value of type [%s] for module [%s] must be provided",
					type.getName(), getTypeName());
				for (final Dependency d : getInternalDependencies().values()) {
					if (d.getType().equals(type)) {
						supplied.put(d.getBeanName(), type.cast(value));
					}
				}
				return this;
			}

			/**
			 * @see com.isotrol.impe3.core.modules.ModuleStarter#start(org.springframework.context.ApplicationContext)
			 */
			public StartedModule<T> start(ApplicationContext parent) {
				final Map<String, Object> suppliedDeps;
				if (configurationDependency == null || !configurationDependency.isRequired() || supplied.containsKey(configurationDependency.getBeanName())) {
					suppliedDeps = Maps.newHashMap(supplied);
				} else {
					checkState(!configuration.hasMBPItems(),"Missing required configuration for module [%s]", getTypeName());
					suppliedDeps = Maps.newHashMap(supplied);
					suppliedDeps.put(getConfigurationBeanName(), configuration.builder().get());
				}
				// Add portal configuration
				if (portalConfigurationDependency != null) {
					suppliedDeps.put(getPortalConfigurationBeanName(), portalConfiguration.builder().get());
				}
				
				final Set<String> required = Maps.filterValues(dependencies, Dependency.IS_REQUIRED).keySet();
				final Set<String> missing = Sets.difference(required, suppliedDeps.keySet());
				checkState(missing.isEmpty(), "Missing required dependencies %s for module [%s]", missing,
					getTypeName());
				// Create context.
				final GenericApplicationContext context = new GenericApplicationContext(parent);
				// Feed original beans except supplied optional dependencies.
				for (final String bean : registry.getBeanDefinitionNames()) {
					if (!suppliedDeps.containsKey(bean)) {
						context.registerBeanDefinition(bean, registry.getBeanDefinition(bean));
					}
				}
				// Feed dependencies
				for (final String bean : suppliedDeps.keySet()) {
					final Dependency d = dependencies.get(bean);
					final BeanDefinition bd = DependencyFactoryBean.getDefinition(d.getType(), suppliedDeps.get(bean));
					context.registerBeanDefinition(bean, bd);
				}
				// Start context.
				context.refresh();
				// Done
				return new Started(context);
			}
		}

		private final class Started implements StartedModule<T> {
			private final GenericApplicationContext context;
			private final T module;

			private Started(GenericApplicationContext context) {
				this.context = context;
				final Class<T> klass = getType();
				@SuppressWarnings("unchecked")
				T proxy = (T) Proxy.newProxyInstance(klass.getClassLoader(), new Class<?>[] {klass}, new Handler());
				this.module = proxy;
			}

			/**
			 * @see com.isotrol.impe3.core.modules.StartedModule#getModuleDefinition()
			 */
			public ModuleDefinition<T> getModuleDefinition() {
				return Valid.this;
			}

			/**
			 * @see com.isotrol.impe3.core.modules.StartedModule#getProvision(java.lang.String)
			 */
			public Object getProvision(String name) {
				checkNotNull(name, "Provision name for module [%s] must be provided", getTypeName());
				checkArgument(provisions.containsKey(name), "Provision [%s] not found in module [%s]", name,
					getTypeName());
				return context.getBean(name);
			}

			public T getModule() {
				return module;
			}

			/**
			 * @see com.isotrol.impe3.core.modules.StartedModule#getAction(com.isotrol.impe3.api.component.ActionContext)
			 */
			public Object getAction(ActionContext actionContext) {
				checkState(moduleType == ModuleType.COMPONENT, "Module [%s] is not a component module", getTypeName());
				final String name = actionContext.getName();
				checkArgument(actions.contains(name), "Component Module [%s] does not provide action [%s]",
					getTypeName(), name);
				// 1 - Instantiate bean
				final Object bean = context.getBean(name);
				// 2 - Injection
				final Route route = actionContext.getRoute();
				final Portal portal = actionContext.getPortal();
				final PrincipalContext principalContext = actionContext.getPrincipalContext();
				for (Method m : bean.getClass().getMethods()) {
					if (m.isAnnotationPresent(Inject.class) && Void.TYPE == m.getReturnType()) {
						final Class<?>[] types = m.getParameterTypes();
						if (types.length == 1) {
							Class<?> type = types[0];
							if (ActionContext.class == type) {
								set(m, bean, actionContext);
							} else if (Portal.class == type) {
								set(m, bean, portal);
							} else if (UUID.class == type) {
								set(m, bean, actionContext.getId());
							} else if (Route.class == type) {
								set(m, bean, route);
							} else if (PageKey.class == type) {
								set(m, bean, route.getPage());
							} else if (Device.class == type) {
								set(m, bean, actionContext.getDevice());
							} else if (Locale.class == type) {
								set(m, bean, actionContext.getLocale());
							} else if (PrincipalContext.class == type) {
								set(m, bean, principalContext);
							} else if (Principal.class == type && principalContext != null) {
								set(m, bean, principalContext.getPrincipal());
							} else if (ContentLoader.class == type) {
								set(m, bean, actionContext.getContentLoader());
							}
						}
					}
				}
				return bean;
			}

			private void set(Method m, Object target, Object value) {
				if (value == null) {
					return;
				}
				try {
					m.invoke(target, value);
				} catch (Exception e) {
					Loggers.core().error(String.format("Unable to inject action method [%s]", m.getName()));
				}
			}

			/**
			 * @see com.isotrol.impe3.core.modules.StartedModule#stop()
			 */
			public void stop() {
				try {
					context.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}

			private final class Handler implements InvocationHandler {
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					if (method.getParameterTypes().length == 0) {
						return getProvision(method.getName());
					}
					throw new UnsupportedOperationException();
				}
			}

		}

	}
}
