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
import java.lang.reflect.Method;


/**
 * Access to annotations with a single string parameter of name {@code value}.
 * @author Andres Rodriguez.
 */
final class StringValue {
	private static final String VALUE = "value";

	/** Not instantiable. */
	private StringValue() {
		throw new AssertionError();
	}

	static <A extends Annotation> String value(A annotation, String defaultValue) {
		if (annotation == null) {
			return defaultValue;
		}
		try {
			final Method m = annotation.annotationType().getMethod(VALUE);
			return (String) m.invoke(annotation);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static <A extends Annotation> String value(A annotation) {
		return value(annotation, "");
	}

	static <A extends Annotation> String value(AnnotatedElement element, Class<A> type, String defaultValue) {
		return value(element.getAnnotation(type), defaultValue);
	}

	static <A extends Annotation> String value(AnnotatedElement element, Class<A> type) {
		return value(element, type, "");
	}

}
