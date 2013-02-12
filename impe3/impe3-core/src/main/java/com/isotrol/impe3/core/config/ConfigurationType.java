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

import java.lang.reflect.Field;
import java.util.Map;

import javax.ws.rs.DefaultValue;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.core.support.NamedSupport;


/**
 * Configuration type.
 * @author Andres Rodriguez.
 */
class ConfigurationType {
	private static final ImmutableSet<String> TRUE = ImmutableSet.of("true", "1", "yes");
	private static final ImmutableSet<String> FALSE = ImmutableSet.of("false", "0", "no");

	/** Value type. */
	private final Class<?> valueType;
	/** Whether the type cannot be optional. */
	private final boolean requiredForced;

	private static final ImmutableMap<Class<?>, ConfigurationType> TYPES;

	static {
		final Map<Class<?>, ConfigurationType> types = Maps.newHashMap();
		types.put(String.class, new StringType());
		types.put(Integer.class, new IntegerType(false));
		types.put(FileId.class, new ConfigurationType(FileId.class));
		types.put(ContentType.class, new ConfigurationType(ContentType.class));
		types.put(Category.class, new ConfigurationType(Category.class));
		types.put(Boolean.class, new BooleanType(false));
		types.put(boolean.class, new BooleanType(true));
		types.put(int.class, new IntegerType(true));
		TYPES = ImmutableMap.copyOf(types);
	}

	static final ConfigurationType of(Class<? extends Configuration> configClass, String name, Class<?> type) {
		if (type.isEnum()) {
			return new EnumType(type);
		}
		final ConfigurationType ct = ConfigurationType.TYPES.get(type);
		if (ct == null) {
			throw new IllegalTypeConfigurationException(configClass, name, type);
		}
		return (ConfigurationType) ct;
	}

	private ConfigurationType(Class<?> valueType, boolean requiredForced) {
		this.valueType = checkNotNull(valueType);
		this.requiredForced = requiredForced;
	}

	private ConfigurationType(Class<?> valueType) {
		this(valueType, false);
	}

	final Class<?> getValueType() {
		return valueType;
	}

	final boolean isRequiredForced() {
		return requiredForced;
	}

	final Object parse(DefaultValue dv) {
		if (dv == null) {
			return null;
		} else {
			return valueType.cast(parse(dv.value()));
		}
	}

	Object parse(String value) {
		throw new IllegalArgumentException();
	}

	boolean isEnum() {
		return false;
	}

	ImmutableMap<Enum<?>, NamedSupport> getChoices() {
		throw new IllegalStateException("Not an enumerated type");
	}

	private static final class StringType extends ConfigurationType {
		StringType() {
			super(String.class);
		}

		@Override
		Object parse(String value) {
			return value;
		}
	}

	private static final class IntegerType extends ConfigurationType {
		IntegerType(boolean requiredForced) {
			super(Integer.class, requiredForced);
		}

		@Override
		Object parse(String value) {
			return Integer.parseInt(value);
		}
	}

	private static final class BooleanType extends ConfigurationType {
		BooleanType(boolean requiredForced) {
			super(Boolean.class, requiredForced);
		}

		@Override
		Object parse(String value) {
			value = value.toLowerCase();
			if (TRUE.contains(value)) {
				return Boolean.TRUE;
			}
			if (FALSE.contains(value)) {
				return Boolean.FALSE;
			}
			throw new IllegalArgumentException();
		}
	}

	private static final class EnumType extends ConfigurationType {
		private final ImmutableMap<Enum<?>, NamedSupport> choices;

		EnumType(Class<?> klass) {
			super(klass, false);
			ImmutableMap.Builder<Enum<?>, NamedSupport> b = ImmutableMap.builder();
			for (Object o : klass.getEnumConstants()) {
				final Enum<?> e = (Enum<?>) o;
				try {
					final Field field = klass.getField(e.name());
					b.put(e, new NamedSupport(field));
				} catch (NoSuchFieldException fe) {
					throw new IllegalArgumentException("Unable to access enum values", fe);
				}
			}
			this.choices = b.build();
			checkArgument(!this.choices.isEmpty(), "Empty enumeration received");
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		@Override
		Object parse(String value) {
			final Class<Enum> e = (Class<Enum>) getValueType();
			return Enum.valueOf(e, value);
		}

		boolean isEnum() {
			return true;
		};

		ImmutableMap<Enum<?>, NamedSupport> getChoices() {
			return choices;
		}

	}
}
