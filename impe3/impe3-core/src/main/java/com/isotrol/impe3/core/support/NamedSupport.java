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

package com.isotrol.impe3.core.support;


import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.sf.derquinsej.i18n.BundleLocalized;
import net.sf.derquinsej.i18n.Localized;
import net.sf.derquinsej.i18n.Unlocalized;

import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Named implementation for types and methods.
 * @author Andres Rodriguez.
 */
public final class NamedSupport implements Named {
	/** Name. */
	private final Localized<String> name;
	/** Description. */
	private final Localized<String> description;

	private static Localized<String> value(Class<?> type, Bundle bundle, Annotation annotation, String defaultValue) {
		if (annotation != null) {
			final String value = StringValue.value(annotation);
			if (value != null && value.length() > 0) {
				if (bundle == null) {
					return Unlocalized.of(value);
				}
				try {
					final Localized<String> ls;
					final String baseName = bundle.value();
					if (baseName == null || baseName.length() == 0) {
						ls = new BundleLocalized<String>(type, value);
					} else {
						ls = new BundleLocalized<String>(baseName, value);
					}
					ls.get();
					return ls;
				} catch (Exception e) {
					// Fallback to default value
				}
			}
		}
		return Unlocalized.of(defaultValue);
	}

	/**
	 * Constructor.
	 * @param type Type to analyze.
	 * @param element Annotated type or method.
	 * @param value Default value.
	 */
	private NamedSupport(Class<?> type, AnnotatedElement element, String value) {
		final Bundle bundle = type.getAnnotation(Bundle.class);
		final Name lname = element.getAnnotation(Name.class);
		final Description ldescription = element.getAnnotation(Description.class);
		this.name = value(type, bundle, lname, value);
		this.description = value(type, bundle, ldescription, "");
	}

	/**
	 * Constructs a named for a type.
	 * @param type Type to analyze.
	 */
	public NamedSupport(Class<?> type) {
		this(type, type, type.getName());
	}

	/**
	 * Constructs a named for a method.
	 * @param method Method to analyze.
	 */
	public NamedSupport(Method method) {
		this(method.getDeclaringClass(), method, method.getName());
	}

	/**
	 * Constructs a named for a field.
	 * @param field Field to analyze.
	 */
	public NamedSupport(Field field) {
		this(field.getDeclaringClass(), field, field.getName());
	}
	
	/**
	 * @see com.isotrol.impe3.core.support.Named#getName()
	 */
	public Localized<String> getName() {
		return name;
	}

	/**
	 * @see com.isotrol.impe3.core.support.Named#getDescription()
	 */
	public Localized<String> getDescription() {
		return description;
	}
}
