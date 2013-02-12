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


import static com.isotrol.impe3.core.component.Components.IS_BE_INJECTABLE;
import static com.isotrol.impe3.core.component.Components.IS_DIRECT_INJECTABLE;

import java.lang.reflect.Method;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMultimap;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.InjectBindingErrors;
import com.isotrol.impe3.api.content.Listing;


/**
 * Collection of direct injectors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public final class DirectInjectors<T extends Component> extends Injectors<T> {
	private final ImmutableMultimap<Class<?>, DirectInjector> injectors;

	static <T extends Component> DirectInjectors<T> direct(Class<T> type, Iterable<Method> methods) {
		return new DirectInjectors<T>(type, IS_DIRECT_INJECTABLE, filter(methods, Inject.class));
	}

	static <T extends Component> DirectInjectors<T> bindingErrors(Class<T> type, Iterable<Method> methods) {
		return new DirectInjectors<T>(type, IS_BE_INJECTABLE, filter(methods, InjectBindingErrors.class));
	}

	private DirectInjectors(final Class<T> type, final Predicate<Class<?>> valid, Iterable<Method> methods) {
		super(type);
		final ImmutableMultimap.Builder<Class<?>, DirectInjector> builder = ImmutableMultimap.builder();
		for (Method m : methods) {
			final DirectInjector i = new DirectInjector(m, valid);
			Class<?> pt = i.getParameterType();
			if (Listing.class.isAssignableFrom(pt)) {
				pt = Listing.class;
			}
			builder.put(pt, i);
		}
		this.injectors = builder.build();
	}

	@Override
	public boolean isEmpty() {
		return injectors.isEmpty();
	}

	Iterable<Class<?>> getInjectedTypes() {
		return injectors.keySet();
	}

	/**
	 * Performs a direct injection.
	 * @param target Target object.
	 * @param valueType Value type.
	 * @param value Value to inject.
	 */
	public final void inject(Object target, Class<?> valueType, Object value) {
		for (final DirectInjector i : injectors.get(valueType)) {
			i.inject(target, value);
		}
	}

	/**
	 * A direct injector.
	 * @author Andres Rodriguez
	 */
	private class DirectInjector extends Injector {
		DirectInjector(final Method method, final Predicate<Class<?>> valid) {
			super(method);
			if (!valid.apply(getParameterType())) {
				throw new IllegalInjectionMethodException(getType(), method);
			}
		}

		/**
		 * Injects the value.
		 * @param target Target object.
		 * @param value Value to inject.
		 */
		public final void inject(Object target, Object value) {
			invoke(target, value);
		}
	}
}
