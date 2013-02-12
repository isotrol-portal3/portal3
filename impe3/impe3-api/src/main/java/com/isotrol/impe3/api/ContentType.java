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
 * Value representing a content type.
 * @author Andres Rodriguez
 */
public final class ContentType extends RoutableNamedIdentifiable {
	public static final Predicate<ContentType> IS_NAVIGABLE = new Predicate<ContentType>() {
		public boolean apply(ContentType input) {
			return input.isNavigable();
		};
	};

	/** Whether the content type is navigable. */
	private final boolean navigable;

	/**
	 * Returns a new builder.
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	private ContentType(Builder builder) {
		super(builder);
		this.navigable = builder.navigable;
	}

	/**
	 * Returns whether the content type is navigable.
	 * @return True if the content type is navigable.
	 */
	public boolean isNavigable() {
		return navigable;
	}
	
	@Override
	public String toString() {
		return String.format("CT[%s:%s]", getStringId(), getDefaultName().getDisplayName());
	}

	/**
	 * Builder for ContentType.
	 * @author Andres Rodriguez
	 */
	public static final class Builder extends RoutableNamedIdentifiable.Builder<Builder, ContentType> {
		/** Whether the content type is navigable. */
		private boolean navigable = true;

		/** Constructor. */
		private Builder() {
		}

		/**
		 * Sets whether the object is navigable.
		 * @param value True if the object is navigable.
		 * @return This builder.
		 */
		public Builder setNavigable(boolean value) {
			this.navigable = value;
			return thisValue();
		}

		/**
		 * Returns the built content type.
		 * @return The built content type.
		 * @throws IllegalStateException in case of an error.
		 */
		protected ContentType doGet() {
			return new ContentType(this);
		}

	}

}
