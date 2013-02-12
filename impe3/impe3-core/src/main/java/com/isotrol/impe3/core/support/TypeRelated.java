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


import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.derquinsej.Methods;


/**
 * Abstract base class for classes that deal with other classes.
 * @author Andres Rodriguez.
 * 
 * @param <T> Type to describe.
 */
public abstract class TypeRelated<T> {
	/** Type to describe. */
	private final Class<T> type;

	protected TypeRelated(final Class<T> type) {
		checkNotNull(type, "A type must be provided");
		this.type = type;
	}

	/**
	 * Returns the described type.
	 * @return The described type.
	 */
	public final Class<T> getType() {
		return type;
	}

	/**
	 * Returns the described type's name.
	 * @return The described type's name.
	 */
	public final String getTypeName() {
		return type.getName();
	}

	/**
	 * Returns the methods of the described type.
	 * @return The methods of the described type.
	 */
	public final List<Method> getMethods() {
		return Methods.getMethods(type);
	}

}
