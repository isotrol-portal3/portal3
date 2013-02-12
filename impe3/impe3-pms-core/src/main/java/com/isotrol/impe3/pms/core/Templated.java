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

package com.isotrol.impe3.pms.core;


import static com.google.common.base.Preconditions.checkNotNull;
import net.sf.derquinsej.collect.Hierarchy;


/**
 * Templated decorator.
 * @author Andres Rodriguez.
 */
public abstract class Templated<T> {

	public static <T> Templated<T> of(final boolean inherited, final T value) {
		return new Value<T>(inherited, checkNotNull(value));
	}

	public static <T> Templated<T> of(final Hierarchy<Integer, Templated<T>> hierarchy) {
		return new Fill<T>(hierarchy);
	}

	private Templated() {
	}

	public abstract boolean isInherited();
	
	public boolean isValue() {
		return false;
	}

	public T getValue() {
		throw new UnsupportedOperationException();
	}

	public Hierarchy<Integer, Templated<T>> getHierarchy() {
		throw new UnsupportedOperationException();
	}

	private static final class Value<T> extends Templated<T> {
		private final boolean inherited;
		private final T value;

		private Value(final boolean inherited, final T value) {
			this.inherited = inherited;
			this.value = value;
		}

		public boolean isValue() {
			return true;
		}

		public boolean isInherited() {
			return inherited;
		}

		@Override
		public T getValue() {
			return value;
		}

	}

	private static final class Fill<T> extends Templated<T> {
		private final Hierarchy<Integer, Templated<T>> hierarchy;

		private Fill(Hierarchy<Integer, Templated<T>> hierarchy) {
			this.hierarchy = checkNotNull(hierarchy);
		}

		public boolean isInherited() {
			return false;
		}

		@Override
		public Hierarchy<Integer, Templated<T>> getHierarchy() {
			return hierarchy;
		}
	}
}
