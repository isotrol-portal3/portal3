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

package com.isotrol.impe3.core.component;


import java.lang.reflect.Method;
import java.util.List;

import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.InjectProperty;


/**
 * Collection of property injectors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public final class PropertyInjectors<T extends Component> extends Injectors<T> {
	private final ImmutableList<PropertyInjector> injectors;

	static <T extends Component> PropertyInjectors<T> of(Class<T> type, Iterable<Method> methods) {
		return new PropertyInjectors<T>(type, filter(methods, InjectProperty.class));
	}

	private PropertyInjectors(Class<T> type, Iterable<Method> methods) {
		super(type);
		final List<PropertyInjector> list = Lists.newLinkedList();
		for (Method method : methods) {
			list.add(new PropertyInjector(method));
		}
		this.injectors = ImmutableList.copyOf(list);
	}

	@Override
	public boolean isEmpty() {
		return injectors.isEmpty();
	}

	/**
	 * Performs the property injection.
	 * @param target Target object.
	 * @param portal Source portal.
	 */
	public void inject(Object target, Portal portal) {
		for (PropertyInjector i : injectors) {
			i.inject(target, portal);
		}
	}

	/**
	 * Injector for portal properties.
	 * @author Andres Rodriguez
	 */
	private final class PropertyInjector extends Injector {
		/** Property name. */
		private final String name;
		/** Whether an engine mode-dependent injection is requested. */
		private final boolean modeDependent;

		PropertyInjector(final Method method) {
			super(method);
			final InjectProperty ann = method.getAnnotation(InjectProperty.class);
			this.name = ann.value();
			this.modeDependent = ann.modeDependent();
			if (!StringUtils.hasText(name) || !String.class.equals(getParameterType())) {
				throw new IllegalInjectionMethodException(getType(), method);
			}
		}

		/**
		 * Performs the parameter injection.
		 * @param target Target object.
		 * @param portal Source portal.
		 */
		void inject(Object target, Portal portal) {
			final Object value = modeDependent ? portal.getMDProperty(name) : portal.getProperty(name);
			if (value != null) {
				invoke(target, value);
			}
		}
	}
}
