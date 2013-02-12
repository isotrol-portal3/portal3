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

import com.isotrol.impe3.api.component.Component;


/**
 * Base class for collections of injectors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public abstract class Injectors<T extends Component> extends Operators<T> {
	Injectors(final Class<T> type) {
		super(type);
	}

	/**
	 * Abstract class for injectors.
	 * @author Andres Rodriguez
	 */
	public abstract class Injector extends Operator {
		Injector(final Method method) {
			super(method, 0);
			if (!Void.TYPE.equals(method.getReturnType()) || method.getParameterTypes().length != 1) {
				throw new IllegalInjectionMethodException(getType(), method);
			}
		}
	}
}
