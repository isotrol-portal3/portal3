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

package com.isotrol.impe3.core.config;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.transform;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.DefaultValue;

import net.sf.derquinsej.Classes;
import net.sf.derquinsej.Methods;
import net.sf.derquinsej.i18n.Localized;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.Compressed;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Downloadable;
import com.isotrol.impe3.api.FileBundle;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.StringItemStyle;
import com.isotrol.impe3.api.StringStyle;
import com.isotrol.impe3.core.support.Definition;
import com.isotrol.impe3.core.support.Named;
import com.isotrol.impe3.core.support.NamedSupport;
import com.isotrol.impe3.core.support.SingleValueSupport;


/**
 * Configuration definition.
 * @author Andres Rodriguez.
 * 
 * @param <T> Configuration interface.
 */
public final class ConfigurationDefinition<T extends Configuration> extends Definition<T> {
	/** Cache. */
	private static final SingleValueSupport<Class<?>, ConfigurationDefinition<?>> CACHE = SingleValueSupport.create();

	/** Predicate to evaluate if a class is a configuration. */
	public static final Predicate<Class<?>> IS_CONFIGURATION = Classes.extendsOrImplements(Configuration.class);

	/** Predicate to evaluate if an item is required. */
	public static final Predicate<Item> IS_REQUIRED = new Predicate<Item>() {
		public boolean apply(Item input) {
			return input.isRequired();
		}
	};

	/** Predicate to evaluate if an item must be provided (is required and has no default value). */
	private static final Predicate<Item> IS_MBP = new Predicate<Item>() {
		public boolean apply(Item input) {
			return input.isMVP();
		}
	};

	/** Function to get the type of an item. */
	public static Function<Item, Class<?>> ITEM_TYPE = new Function<Item, Class<?>>() {
		public Class<?> apply(Item from) {
			return from.getType();
		}
	};

	/**
	 * Returns the configuration definition for the specified configuration interface.
	 * @param configClass Configuration class.
	 * @return The configuration definition.
	 * @throws ConfigurationException if the configuration class is invalid.
	 */
	public static <T extends Configuration> ConfigurationDefinition<T> of(Class<T> configClass)
		throws ConfigurationException {
		Preconditions.checkNotNull(configClass, "A configuration class must be provided");
		@SuppressWarnings("unchecked")
		final ConfigurationDefinition<T> d1 = (ConfigurationDefinition<T>) CACHE.get(configClass);
		if (d1 != null) {
			return d1;
		}
		@SuppressWarnings("unchecked")
		final ConfigurationDefinition<T> d2 = (ConfigurationDefinition<T>) CACHE.put(configClass,
			new ConfigurationDefinition<T>(configClass));
		return d2;
	}

	/**
	 * Returns the configuration definition for the specified configuration interface.
	 * @param name Configuration class name.
	 * @return The configuration definition.
	 * @throws ConfigurationException if the configuration class is invalid.
	 * @throws IllegalArgumentException if the name is not a configuration class.
	 */
	public static ConfigurationDefinition<?> of(String name) throws ConfigurationException {
		Preconditions.checkNotNull(name, "A configuration class name must be provided");
		try {
			final Class<?> klass = Class.forName(name);
			final Class<? extends Configuration> configClass = klass.asSubclass(Configuration.class);
			return of(configClass);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static ImmutableMap<String, Item> getItems(Class<? extends Configuration> configClass)
		throws ConfigurationException {
		if (!configClass.isInterface()) {
			throw new NonInterfaceConfigurationException(configClass);
		}
		final List<Method> methods = Methods.getMethods(configClass);
		if (methods.isEmpty()) {
			throw new EmptyConfigurationException(configClass);
		}
		final Map<String, Item> map = Maps.newHashMap();
		for (Method m : methods) {
			final Item item = new Item(configClass, m);
			map.put(item.getParameter(), item);
		}
		return ImmutableMap.copyOf(map);
	}

	/** Parameters map. */
	private final ImmutableMap<String, Item> parameters;
	/** Whether there are required items. */
	private final boolean required;
	/** Whether there are MVP items. */
	private final boolean mbp;
	/** If the configuration has a content type item. */
	private final boolean contentTypes;
	/** If the configuration has a category item. */
	private final boolean categories;

	/**
	 * Constructs the configuration definition for the specified configuration interface.
	 * @param configClass Configuration class.
	 * @throws ConfigurationException if the configuration class is invalid.
	 */
	private ConfigurationDefinition(Class<T> configClass) throws ConfigurationException {
		super(configClass);
		this.parameters = getItems(configClass);
		this.required = any(this.parameters.values(), IS_REQUIRED);
		this.mbp = any(this.parameters.values(), IS_MBP);
		final Iterable<Class<?>> types = transform(this.parameters.values(), ITEM_TYPE);
		this.contentTypes = any(types, Predicates.<Class<?>> equalTo(ContentType.class));
		this.categories = any(types, Predicates.<Class<?>> equalTo(Category.class));
	}

	/**
	 * Returns the configuration parameters.
	 * @return The configuration parameters.
	 */
	public ImmutableMap<String, Item> getParameters() {
		return parameters;
	}

	/**
	 * Returns the required configuration parameters.
	 * @return The required configuration parameters.
	 */
	public Map<String, Item> getRequiredParameters() {
		return Maps.filterValues(parameters, IS_REQUIRED);
	}

	/**
	 * Returns the configuration parameters that are required and have no default value.
	 * @return The "must be provided" configuration parameters.
	 */
	public Map<String, Item> getMBPParameters() {
		return Maps.filterValues(parameters, IS_MBP);
	}

	public boolean isRequired() {
		return required;
	}

	public boolean hasMBPItems() {
		return mbp;
	}

	public boolean hasContentTypes() {
		return contentTypes;
	}

	public boolean hasCategories() {
		return categories;
	}

	/**
	 * Returns a new builder for the current configuration.
	 * @return
	 */
	public ConfigurationBuilder<T> builder() {
		return new Builder();
	}

	/**
	 * A configuration item.
	 * @author Andres Rodriguez.
	 */
	public static final class Item implements Named {
		private final String parameter;
		private final ConfigurationType type;
		/** Default value. */
		private final Object defaultValue;
		/** Name information. */
		private final NamedSupport name;
		private final boolean required;
		private final boolean downloadable;
		private final boolean bundle;
		private final boolean compressed;
		/** String style. */
		private final StringItemStyle stringStyle;

		private Item(Class<? extends Configuration> configClass, final Method m) {
			this.parameter = m.getName();
			this.name = new NamedSupport(m);
			if (m.getParameterTypes().length > 0) {
				throw new MethodWithArgumentsConfigurationException(configClass, parameter);
			}
			final Class<?> klass = m.getReturnType();
			this.type = ConfigurationType.of(configClass, parameter, klass);
			this.required = this.type.isRequiredForced() || (m.getAnnotation(Optional.class) == null);
			if (FileId.class.equals(klass)) {
				this.downloadable = m.isAnnotationPresent(Downloadable.class);
				this.bundle = m.isAnnotationPresent(FileBundle.class);
				this.compressed = this.bundle ? false : m.isAnnotationPresent(Compressed.class);
			} else {
				this.downloadable = false;
				this.bundle = false;
				this.compressed = false;
			}
			if (String.class.equals(klass) && m.isAnnotationPresent(StringStyle.class)) {
				this.stringStyle = Objects.firstNonNull(m.getAnnotation(StringStyle.class).value(), StringItemStyle.NORMAL);
			} else {
				this.stringStyle = StringItemStyle.NORMAL;
			}
			final DefaultValue dv = m.getAnnotation(DefaultValue.class);
			try {
				this.defaultValue = this.type.parse(dv);
			} catch (RuntimeException e) {
				throw new IllegalValueConfigurationException(configClass, parameter, klass, dv.value());
			}
		}

		public String getParameter() {
			return parameter;
		}

		public Class<?> getType() {
			return type.getValueType();
		}

		/**
		 * Returns the default value.
		 * @return The default value.
		 */
		public Object getDefaultValue() {
			return defaultValue;
		}

		/**
		 * @see com.isotrol.impe3.core.support.Named#getName()
		 */
		public Localized<String> getName() {
			return name.getName();
		}

		/**
		 * @see com.isotrol.impe3.core.support.Named#getDescription()
		 */
		public Localized<String> getDescription() {
			return name.getDescription();
		}

		public boolean isRequired() {
			return required;
		}

		public boolean isMVP() {
			return required && defaultValue == null;
		}

		public boolean isDownloadable() {
			return downloadable;
		}

		public boolean isBundle() {
			return bundle;
		}

		public boolean isCompressed() {
			return compressed;
		}
		
		public StringItemStyle getStringStyle() {
			return stringStyle;
		}

		public boolean isEnum() {
			return type.isEnum();
		}
		
		public Object fromString(String value) {
			if (value == null) {
				return null;
			}
			return type.parse(value);
		}

		public ImmutableMap<Enum<?>, NamedSupport> getChoices() {
			return type.getChoices();
		}

	}

	/**
	 * Builder for configurations objects.
	 * @author Andres Rodriguez.
	 */
	private final class Builder implements ConfigurationBuilder<T> {
		/** Values. */
		private final Map<String, Object> values;

		private Builder() {
			values = Maps.newHashMap();
			for (Item item : parameters.values()) {
				final Object defaultValue = item.getDefaultValue();
				if (defaultValue != null) {
					values.put(item.getParameter(), defaultValue);
				}
			}
		}

		/**
		 * Sets a configuration value.
		 * @param parameter Parameter name.
		 * @param value Parameter value.
		 * @return This builder for method chaining.
		 * @throws NullPointerException if the parameter is null.
		 * @throws IllegalArgumentException if the parameter does not exist or the value is of an incorrect type.
		 */
		public Builder set(String parameter, Object value) {
			checkNotNull(parameter, "A parameter name must be provided");
			checkArgument(parameters.containsKey(parameter), "Invalid parameter [%s] of configuration [%s]", parameter,
				getTypeName());
			if (value == null) {
				values.remove(parameter);
			} else {
				final Class<?> type = parameters.get(parameter).getType();
				checkArgument(type.isInstance(value), "Invalid type [%s] for parameter [%s] of configuration [%s]",
					value.getClass().getName(), parameter, getTypeName());
				values.put(parameter, value);
			}
			return this;
		}

		/**
		 * @see com.google.common.base.Supplier#get()
		 */
		@SuppressWarnings("unchecked")
		public T get() {
			// Compute the difference.
			final Set<String> required = Maps.filterValues(parameters, IS_REQUIRED).keySet();
			final Set<String> diff = Sets.difference(required, values.keySet());
			checkState(diff.isEmpty(), "Missing parameters %s for configuration [%s]", diff, getTypeName());
			final InvocationHandler h = new ConfigurationProxy(ImmutableMap.copyOf(values));
			final Class<?> type = getType();
			return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, h);
		}
	}

	private static final class ConfigurationProxy implements InvocationHandler {
		/** Values. */
		private final ImmutableMap<String, Object> values;

		private ConfigurationProxy(ImmutableMap<String, Object> values) {
			this.values = values;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method,
		 * java.lang.Object[])
		 */
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return values.get(method.getName());
		}
	}
}
