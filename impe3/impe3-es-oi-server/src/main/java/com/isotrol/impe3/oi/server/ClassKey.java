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
package com.isotrol.impe3.oi.server;


import com.google.common.base.Objects;


/**
 * Value that represents a class key.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class ClassKey {
	/** Class set. */
	private final long set;
	/** Class name. */
	private final long name;
	/** Hash code. */
	private final int hash;

	public ClassKey(long set, long name) {
		this.set = set;
		this.name = name;
		this.hash = Objects.hashCode(set, name);
	}

	/**
	 * Returns the class set.
	 * @return The class set.
	 */
	public long getSet() {
		return set;
	}

	/**
	 * Returns the class name.
	 * @return The class name.
	 */
	public long getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ClassKey) {
			final ClassKey k = (ClassKey) obj;
			return hash == k.hash && set == k.set && name == k.name;
		}
		return false;
	}
}
