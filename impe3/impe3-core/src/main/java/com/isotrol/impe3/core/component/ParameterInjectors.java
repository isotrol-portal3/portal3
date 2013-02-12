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
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.api.Headers;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.Parameters;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.SessionParams;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.InjectCookie;
import com.isotrol.impe3.api.component.InjectHeader;
import com.isotrol.impe3.api.component.InjectLocal;
import com.isotrol.impe3.api.component.InjectRequest;
import com.isotrol.impe3.api.component.InjectSession;


/**
 * Collection of parameter injectors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 * @param <S> Type of the source of values.
 */
public final class ParameterInjectors<T extends Component, S extends Parameters<?>> extends Injectors<T> {
	/** Binder. */
	private final Binder<S> binder;
	private final ImmutableList<ParameterInjector> injectors;

	static <T extends Component> ParameterInjectors<T, Cookies> cookies(Class<T> type, Iterable<Method> methods) {
		return new ParameterInjectors<T, Cookies>(type, Binder.COOKIE, filter(methods, InjectCookie.class));
	}

	static <T extends Component> ParameterInjectors<T, Headers> headers(Class<T> type, Iterable<Method> methods) {
		return new ParameterInjectors<T, Headers>(type, Binder.HEADER, filter(methods, InjectHeader.class));
	}

	static <T extends Component> ParameterInjectors<T, RequestParams> request(Class<T> type, Iterable<Method> methods) {
		return new ParameterInjectors<T, RequestParams>(type, Binder.REQUEST, filter(methods, InjectRequest.class));
	}

	static <T extends Component> ParameterInjectors<T, SessionParams> session(Class<T> type, Iterable<Method> methods) {
		return new ParameterInjectors<T, SessionParams>(type, Binder.SESSION, filter(methods, InjectSession.class));
	}

	static <T extends Component> ParameterInjectors<T, LocalParams> local(Class<T> type, Iterable<Method> methods) {
		return new ParameterInjectors<T, LocalParams>(type, Binder.LOCAL, filter(methods, InjectLocal.class));
	}

	private ParameterInjectors(Class<T> type, Binder<S> binder, Iterable<Method> methods) {
		super(type);
		this.binder = binder;
		final List<ParameterInjector> list = Lists.newLinkedList();
		for (Method method : methods) {
			list.add(new ParameterInjector(method));
		}
		this.injectors = ImmutableList.copyOf(list);
	}

	@Override
	public boolean isEmpty() {
		return injectors.isEmpty();
	}

	/**
	 * Performs the parameter injection.
	 * @param target Target object.
	 * @param parameters Source parameters.
	 * @return The parameters that caused binding errors.
	 */
	public S inject(Object target, S parameters) {
		Set<String> rejected = null;
		for (ParameterInjector i : injectors) {
			try {
				i.inject(target, parameters);
			} catch (BindingException e) {
				if (rejected == null) {
					rejected = Sets.newHashSet();
				}
				rejected.add(i.name);
			}
		}
		return binder.subset(parameters, rejected);
	}

	/**
	 * Injector for HTTP parameters, cookies and headers.
	 * @author Andres Rodriguez
	 */
	private final class ParameterInjector extends Injector {
		/** Parameter name. */
		private final String name;

		ParameterInjector(final Method method) {
			super(method);
			this.name = binder.getName(method);
			if (name == null || name.length() == 0 || !binder.isAllowedType(getMethodParameter())) {
				throw new IllegalInjectionMethodException(getType(), method);
			}
		}

		/**
		 * Performs the parameter injection.
		 * @param target Target object.
		 * @param parameters Request parameters.
		 */
		void inject(Object target, S parameters) throws BindingException {
			final Object value = binder.bind(getMethodParameter(), parameters, name);
			if (value != null) {
				invoke(target, value);
			}
		}
	}
}
