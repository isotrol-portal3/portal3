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

package com.isotrol.impe3.api;


import com.google.common.base.Predicate;


/**
 * Abstract class for identifiables with a localized name and a routable flag.
 * @author Andres Rodriguez
 */
public abstract class RoutableNamedIdentifiable extends NamedIdentifiable {
	public static final Predicate<RoutableNamedIdentifiable> IS_ROUTABLE = new Predicate<RoutableNamedIdentifiable>() {
		public boolean apply(RoutableNamedIdentifiable input) {
			return input.isRoutable();
		};
	};

	/** If the object is routable. */
	private final boolean routable;

	/**
	 * Constructor.
	 * @param builder Builder.
	 */
	protected RoutableNamedIdentifiable(Builder<?, ?> builder) {
		super(builder);
		this.routable = builder.routable;
	}

	public final boolean isRoutable() {
		return routable;
	}

	/**
	 * Builder for RoutableNamedIdentifiable.
	 * @author Andres Rodriguez
	 */
	public abstract static class Builder<B extends Builder<B, T>, T extends RoutableNamedIdentifiable> extends
		NamedIdentifiable.Builder<B, T> {
		/** If the object is routable. */
		private boolean routable = true;

		/** Constructor. */
		protected Builder() {
		}

		/**
		 * Sets whether the object is routable.
		 * @param value True if the object is routable.
		 * @return This builder.
		 */
		public B setRoutable(boolean value) {
			routable = value;
			return thisValue();
		}
	}

}
