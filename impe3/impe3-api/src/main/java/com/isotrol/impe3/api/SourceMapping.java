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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


/**
 * Value that represents a source mapping.
 * @author Andres Rodriguez
 */
public final class SourceMapping extends AbstractIdentifiable {
	/** Null ID error message. */
	private static final String NAME_ERROR = "A source mapping name must be provided.";

	/**
	 * Returns a new builder.
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/** Mapping name. */
	private final String name;
	/** Mapping description. */
	private final String description;
	/** Set mappings. */
	private final ImmutableList<SetMapping> sets;
	/** Content type mappings. */
	private final ImmutableList<ContentTypeMapping> contentTypes;
	/** Category mappings. */
	private final ImmutableList<CategoryMapping> categories;

	/**
	 * Constructor.
	 * @param builder Builder.
	 */
	private SourceMapping(Builder builder) {
		super(builder);
		this.name = builder.name;
		this.description = builder.description;
		this.sets = ImmutableList.copyOf(builder.sets);
		this.contentTypes = ImmutableList.copyOf(builder.contentTypes);
		this.categories = ImmutableList.copyOf(builder.categories);
	}

	/**
	 * Returns the mapping name.
	 * @return The mapping name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the mapping description.
	 * @return The mapping description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the set mappings.
	 * @return The set mappings.
	 */
	public List<SetMapping> getSets() {
		return sets;
	}

	/**
	 * Returns the content type mappings.
	 * @return The content type mappings.
	 */
	public List<ContentTypeMapping> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Returns the category mappings.
	 * @return The category mappings.
	 */
	public List<CategoryMapping> getCategories() {
		return categories;
	}

	/**
	 * Builder for SourceMapping.
	 * @author Andres Rodriguez
	 */
	public static final class Builder extends AbstractIdentifiable.Builder<Builder, SourceMapping> {
		/** Mapping name. */
		private String name;
		/** Mapping description. */
		private String description;
		/** Set mappings. */
		private List<SetMapping> sets = Lists.newArrayList();
		/** Content type mappings. */
		private List<ContentTypeMapping> contentTypes = Lists.newArrayList();
		/** Category mappings. */
		private List<CategoryMapping> categories = Lists.newArrayList();

		/** Constructor. */
		private Builder() {
		}

		/**
		 * Sets the mapping name.
		 * @param name The mapping name.
		 * @return This builder.
		 */
		public Builder setName(String name) {
			this.name = checkNotNull(name, NAME_ERROR);
			return this;
		}

		/**
		 * Sets the mapping description.
		 * @param description The mapping description.
		 * @return This builder.
		 */
		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		/**
		 * Adds a content type mapping.
		 * @param mapping Mapping to add.
		 * @return This builder.
		 */
		public Builder addSet(SetMapping mapping) {
			sets.add(checkNotNull(mapping, "A set mapping must be provided"));
			return this;
		}

		/**
		 * Adds a collection of content type mappings.
		 * @param mappings Mappings to add.
		 * @return This builder.
		 */
		public Builder addSets(Iterable<? extends SetMapping> mappings) {
			if (mappings != null) {
				for (SetMapping m : mappings) {
					addSet(m);
				}
			}
			return this;
		}

		/**
		 * Adds a content type mapping.
		 * @param mapping Mapping to add.
		 * @return This builder.
		 */
		public Builder addContentType(ContentTypeMapping mapping) {
			contentTypes.add(checkNotNull(mapping, "A content type mapping must be provided"));
			return this;
		}

		/**
		 * Adds a collection of content type mappings.
		 * @param mappings Mappings to add.
		 * @return This builder.
		 */
		public Builder addContentTypes(Iterable<? extends ContentTypeMapping> mappings) {
			if (mappings != null) {
				for (ContentTypeMapping m : mappings) {
					addContentType(m);
				}
			}
			return this;
		}

		/**
		 * Adds a category mapping.
		 * @param mapping Mapping to add.
		 * @return This builder.
		 */
		public Builder addCategory(CategoryMapping mapping) {
			categories.add(checkNotNull(mapping, "A category mapping must be provided"));
			return this;
		}

		/**
		 * Adds a collection of category mappings.
		 * @param mappings Mappings to add.
		 * @return This builder.
		 */
		public Builder addCategories(Iterable<? extends CategoryMapping> mappings) {
			if (mappings != null) {
				for (CategoryMapping m : mappings) {
					addCategory(m);
				}
			}
			return this;
		}

		@Override
		public void checkState() {
			super.checkState();
			Preconditions.checkState(name != null, NAME_ERROR);
		}

		/**
		 * Returns the built source mapping.
		 * @return The built source mapping.
		 * @throws IllegalStateException in case of an error.
		 */
		protected SourceMapping doGet() {
			return new SourceMapping(this);
		}

	}

}
