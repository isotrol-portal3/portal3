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
package com.isotrol.impe3.idx.d6;


import java.util.Set;


/**
 * Represents a resource type configured by indexer.
 */
public class ResourceTypeDescriptor {

	private String name;

	private Set<Field> customFields;

	/**
	 * Constructor
	 */
	public ResourceTypeDescriptor(String name, Set<Field> customFields) {
		this.name = name;
		this.customFields = customFields;
	}

	public String getName() {
		return name;
	}

	public Set<Field> getCustomFields() {
		return customFields;
	}

	/**
	 * Represents a index field for a resource type.
	 */
	public static class Field {

		private String name;
		private boolean stored;
		private boolean tokenized;
		private String value;
		private String function;

		/**
		 * Constructor
		 */
		public Field(String name, boolean stored, boolean tokenized, String value, String function) {
			this.name = name;
			this.stored = stored;
			this.tokenized = tokenized;
			this.value = value;
			this.function = function;
		}

		/**
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Field) {
				Field other = (Field) obj;
				return name.equals(other.getName());
			} else {
				return false;
			}
		}

		/**
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return name.hashCode();
		}

		public String getName() {
			return name;
		}

		public boolean isStored() {
			return stored;
		}

		public boolean isTokenized() {
			return tokenized;
		}

		public String getValue() {
			return value;
		}

		public String getFunction() {
			return function;
		}
	}
}
