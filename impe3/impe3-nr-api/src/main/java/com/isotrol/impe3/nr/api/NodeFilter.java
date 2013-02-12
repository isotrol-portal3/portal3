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

package com.isotrol.impe3.nr.api;


import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Optional;


/**
 * Node filter. A null filter performs no filtering. An empty filter always returns an empty result. Filters are
 * "joined" in the the sense that operations are applied in order with the following criteria: <ul> <li>A null filter
 * performs no filtering.</li> <li>For an empty filter every operation returns the empty filter.</li> <li>Once due nodes
 * are requested, undue nodes will never be returned.</li> <li>For filter maps, OPTIONAL elements are OR'd together. The
 * resulting filter is NOT'd with the FORBIDDEN and the resulting is AND'd with the REQUIRED.</li> <li>For the
 * categories map, the uncategorized property is treated as another entry.</li> </ul>
 * @see FilterType for the filter type join results.
 * 
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public abstract class NodeFilter implements Serializable, IsEmptyFlag, IsNullFlag {
	/** Serial UID. */
	private static final long serialVersionUID = 4502350695776878546L;
	/** Null filter. */
	private static final NullNodeFilter NULL_FILTER = new NullNodeFilter();
	/** Empty filter. */
	private static final EmptyNodeFilter EMPTY_FILTER = new EmptyNodeFilter();

	/** Returns the empty filter. */
	public static NodeFilter emptyFilter() {
		return EMPTY_FILTER;
	}

	/** Returns the null filter. */
	public static NodeFilter nullFilter() {
		return NULL_FILTER;
	}

	/** Checks whether a filter is null. */
	public static boolean isNull(NodeFilter filter) {
		return filter == null || filter.isNull();
	}

	/** Checks whether a filter is empty. */
	public static boolean isEmpty(NodeFilter filter) {
		return filter != null && filter.isEmpty();
	}

	/** Creates a new builder. */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Constructor.
	 */
	NodeFilter() {
	}

	/** Returns whether to include only released and non-expired nodes. */
	public abstract boolean isDue();

	/**
	 * Returns the node sets criteria.
	 */
	public abstract Map<String, FilterType> sets();

	/**
	 * Returns the categories criteria.
	 */
	public abstract Map<Optional<UUID>, FilterType> categories();

	/**
	 * Returns the content types criteria.
	 */
	public abstract Map<UUID, FilterType> contentTypes();

	/**
	 * Returns the locales criteria.
	 */
	public abstract Map<Locale, FilterType> locales();

	/**
	 * Returns the tags criteria.
	 */
	public abstract Map<String, FilterType> tags();

	/**
	 * Returns the node keys criteria.
	 */
	public abstract Map<NodeKey, FilterType> keys();

	/** Node filter builder. */
	public static final class Builder implements Cloneable {
		/** Only due nodes. */
		private boolean due;
		/** Sets. */
		private final SetsCriteria sets;
		/** Categories. */
		private final CategoriesCriteria categories;
		/** Content types. */
		private final ContentTypesCriteria contentTypes;
		/** Locales. */
		private final LocalesCriteria locales;
		/** Tags. */
		private final TagsCriteria tags;
		/** Node keys. */
		private final KeysCriteria keys;

		/** Constructor. */
		Builder() {
			this.due = false;
			this.sets = new SetsCriteria(this);
			this.categories = new CategoriesCriteria(this);
			this.contentTypes = new ContentTypesCriteria(this);
			this.locales = new LocalesCriteria(this);
			this.tags = new TagsCriteria(this);
			this.keys = new KeysCriteria(this);
		}

		/** Copy constructor. */
		private Builder(Builder b) {
			this.due = b.due;
			this.sets = new SetsCriteria(this, b.sets);
			this.categories = new CategoriesCriteria(this, b.categories);
			this.contentTypes = new ContentTypesCriteria(this, b.contentTypes);
			this.locales = new LocalesCriteria(this, b.locales);
			this.tags = new TagsCriteria(this, b.tags);
			this.keys = new KeysCriteria(this, b.keys);
		}

		public Builder clone() {
			return new Builder(this);
		}
		
		public boolean isDue() {
			return due;
		}


		/** Sets whether only due nodes should be returned. */
		public Builder due(boolean due) {
			if (!this.due && due) {
				this.due = true;
			}
			return this;
		}

		/**
		 * Returns the node sets criteria.
		 */
		public SetsCriteria sets() {
			return sets;
		}

		/**
		 * Returns the categories criteria.
		 */
		public CategoriesCriteria categories() {
			return categories;
		}

		/**
		 * Applies the provided criterion to the uncategorized nodes.
		 * @param type Filter type.
		 * @return The filter builder for method chaining.
		 * @throws NullPointerException if the filter type is {@code null}.
		 */
		public final Builder uncategorized(FilterType type) {
			return categories.uncategorized(type);
		}

		/**
		 * Returns the content types criteria.
		 */
		public ContentTypesCriteria contentTypes() {
			return contentTypes;
		}

		/**
		 * Returns the locales criteria.
		 */
		public LocalesCriteria locales() {
			return locales;
		}

		/**
		 * Returns the tags criteria.
		 */
		public TagsCriteria tags() {
			return tags;
		}

		/**
		 * Returns the node keys criteria.
		 */
		public KeysCriteria keys() {
			return keys;
		}

		/** Creates the node filter. */
		public NodeFilter build() {
			if (sets.isEmpty() || categories.isEmpty() || contentTypes.isEmpty() || locales.isEmpty() || tags.isEmpty()
				|| keys.isEmpty()) {
				return emptyFilter();
			}
			if (!due && sets.isNull() && categories.isNull() && contentTypes.isNull() && locales.isNull()
				&& tags.isNull() && keys.isNull()) {
				return nullFilter();
			}
			return new RegularNodeFilter(due, sets.get(), categories.get(), contentTypes.get(), locales.get(),
				tags.get(), keys.get());
		}
	}

}
