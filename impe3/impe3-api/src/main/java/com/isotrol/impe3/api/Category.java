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
 * Value representing a category.
 * @author Andres Rodriguez
 */
public final class Category extends RoutableNamedIdentifiable {
	public static final Predicate<Category> VISIBLE = new Predicate<Category>() {
		public boolean apply(Category input) {
			return input.isVisible();
		};
	};

	/**
	 * Returns a new builder.
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/** If the category is visible. */
	private final boolean visible;

	private Category(Builder builder) {
		super(builder);
		this.visible = builder.visible;
	}

	public boolean isVisible() {
		return visible;
	}

	@Override
	public String toString() {
		return String.format("CG[%s:%s]", getStringId(), getDefaultName().getDisplayName());
	}

	/**
	 * Builder for Category.
	 * @author Andres Rodriguez
	 */
	public static final class Builder extends RoutableNamedIdentifiable.Builder<Builder, Category> {
		/** If the object is visible. */
		private boolean visible = true;

		/** Constructor. */
		private Builder() {
		}

		/**
		 * Sets whether the object is visible.
		 * @param value True if the object is visible.
		 * @return This builder.
		 */
		public Builder setVisible(boolean value) {
			visible = value;
			return thisValue();
		}

		/**
		 * Returns the built content type.
		 * @return The built content type.
		 * @throws IllegalStateException in case of an error.
		 */
		protected Category doGet() {
			return new Category(this);
		}

	}

}
