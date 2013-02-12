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


import static com.google.common.base.Preconditions.checkArgument;
import static net.sf.derquinsej.Methods.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.MethodParameter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.core.support.TypeRelated;


/**
 * Base class for collections of injectors and extractors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
abstract class Operators<T extends Component> extends TypeRelated<T> {
	Operators(final Class<T> type) {
		super(type);
	}

	static Iterable<Method> filter(Iterable<Method> methods, Class<? extends Annotation> annotation) {
		return Iterables.filter(methods, annotated(annotation));
	}

	/**
	 * Returns true if the collection contains any operator.
	 */
	public abstract boolean isEmpty();

	/**
	 * Base class for injectors and extractors.
	 * @author Andres Rodriguez
	 */
	abstract class Operator {
		private final MethodParameter methodParameter;

		Operator(final Method method, int index) {
			Preconditions.checkNotNull(method);
			this.methodParameter = new MethodParameter(method, index);
		}

		final MethodParameter getMethodParameter() {
			return methodParameter;
		}

		final Method getMethod() {
			return methodParameter.getMethod();
		}

		final Class<?> getParameterType() {
			if (methodParameter.getParameterIndex() < 0) {
				return methodParameter.getMethod().getReturnType();
			}
			return methodParameter.getParameterType();
		}

		final Class<?> getReturnType() {
			return getMethod().getReturnType();
		}

		final Class<?> getCollectionParameterType() {
			return GenericCollectionTypeResolver.getCollectionParameterType(methodParameter);
		}

		final Object invoke(Object target, Object... arguments) {
			checkArgument(getType().isInstance(target));
			try {
				return getMethod().invoke(target, arguments);
			} catch (Exception e) {
				e.printStackTrace();
				throw getException(e);
			}
		}

		RuntimeException getException(Exception e) {
			return new RuntimeException(e);
		}
	}
}
