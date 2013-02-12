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


import static org.springframework.core.GenericCollectionTypeResolver.getCollectionParameterType;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Cookie;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.validation.DataBinder;

import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.api.Headers;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.Parameters;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.SessionParams;
import com.isotrol.impe3.api.component.InjectCookie;
import com.isotrol.impe3.api.component.InjectHeader;
import com.isotrol.impe3.api.component.InjectLocal;
import com.isotrol.impe3.api.component.InjectRequest;
import com.isotrol.impe3.api.component.InjectSession;


/**
 * Binders for parameter-based injectors.
 * @author Andres Rodriguez
 * @param <S> Type of the source of values.
 */
abstract class Binder<S extends Parameters<?>> {
	private Binder() {
	}

	abstract String getName(Method method);

	boolean isAllowedType(MethodParameter mp) {
		return true;
	}

	abstract Object bind(MethodParameter mp, S parameters, String name) throws BindingException;

	@SuppressWarnings("unchecked")
	final S subset(S original, Set<String> rejected) {
		return (S) original.subset(rejected);
	}

	final Object convert(MethodParameter mp, Object value) throws BindingException {
		if (value == null) {
			return null;
		}
		try {
			final DataBinder binder = new DataBinder(null);
			return binder.convertIfNecessary(value, mp.getParameterType(), mp);
		} catch (TypeMismatchException e) {
			throw new BindingException(e);
		}
	}

	static final Binder<Headers> HEADER = new Binder<Headers>() {
		String getName(Method method) {
			return method.getAnnotation(InjectHeader.class).value();
		};

		@Override
		boolean isAllowedType(MethodParameter mp) {
			final Class<?> type = mp.getParameterType();
			return (type == String.class) || (type == List.class && getCollectionParameterType(mp) == String.class);
		}

		@Override
		Object bind(MethodParameter mp, Headers parameters, String name) throws BindingException {
			final List<String> values = parameters.get(name);
			final boolean list = (mp.getParameterType() != String.class);
			if (list) {
				return values;
			} else if (values != null && !values.isEmpty()) {
				return values.get(0);
			} else {
				return null;
			}
		}

	};

	static final Binder<Cookies> COOKIE = new Binder<Cookies>() {
		String getName(Method method) {
			return method.getAnnotation(InjectCookie.class).value();
		};

		@Override
		boolean isAllowedType(MethodParameter mp) {
			return !Collection.class.isAssignableFrom(mp.getParameterType());
		}

		@Override
		Object bind(MethodParameter mp, Cookies parameters, String name) throws BindingException {
			final Cookie c = parameters.get(name);
			if (c == null) {
				return null;
			}
			if (mp.getParameterType() == Cookie.class) {
				return c;
			}
			return convert(mp, c.getValue());
		}

	};

	static final Binder<RequestParams> REQUEST = new Binder<RequestParams>() {
		String getName(Method method) {
			return method.getAnnotation(InjectRequest.class).value();
		};

		@Override
		Object bind(MethodParameter mp, RequestParams parameters, String name) throws BindingException {
			if (Collection.class.isAssignableFrom(mp.getParameterType())) {
				return convert(mp, parameters.get(name));
			} else {
				return convert(mp, parameters.getFirst(name));
			}
		}

	};

	static final Binder<SessionParams> SESSION = new Binder<SessionParams>() {
		String getName(Method method) {
			return method.getAnnotation(InjectSession.class).value();
		};

		@Override
		Object bind(MethodParameter mp, SessionParams parameters, String name) throws BindingException {
			return convert(mp, parameters.get(name));
		}

	};

	static final Binder<LocalParams> LOCAL = new Binder<LocalParams>() {
		String getName(Method method) {
			return method.getAnnotation(InjectLocal.class).value();
		};

		@Override
		Object bind(MethodParameter mp, LocalParams parameters, String name) throws BindingException {
			return convert(mp, parameters.get(name));
		}

	};
	
}
