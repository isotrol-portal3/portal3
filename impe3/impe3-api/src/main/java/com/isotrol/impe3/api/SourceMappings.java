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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;


/**
 * Source mappings collection.
 * @author Andres Rodriguez
 */
public final class SourceMappings extends ForwardingMap<UUID, SourceMapping> {
	/** By Id. */
	private final ImmutableMap<UUID, SourceMapping> byId;
	/** By name. */
	private final ImmutableMap<String, SourceMapping> byName;
	
	/**
	 * Returns a new builder.
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Constructor.
	 * @param builder Builder.
	 */
	private SourceMappings(Builder builder) {
		this.byId = ImmutableMap.copyOf(builder.byId);
		this.byName = ImmutableMap.copyOf(builder.byName);
	}

	@Override
	protected Map<UUID, SourceMapping> delegate() {
		return byId;
	}

	/**
	 * Returns a source mapping by name.
	 * @param name Requested name.
	 * @return The source mapping with the requested name or {@code null} if not found.
	 */
	public SourceMapping getByName(String name) {
		return byName.get(name);
	}

	/**
	 * Builder for SourceMappings.
	 * @author Andres Rodriguez
	 */
	public static final class Builder implements com.isotrol.impe3.api.Builder<SourceMappings> {
		/** By Id. */
		private final Map<UUID, SourceMapping> byId = Maps.newHashMap();
		/** By name. */
		private final Map<String, SourceMapping> byName = Maps.newHashMap();

		/** Constructor. */
		private Builder() {
		}

		/**
		 * Adds a source mapping.
		 * @param mapping Mapping to add.
		 * @return This builder.
		 */
		public Builder add(SourceMapping mapping) {
			checkNotNull(mapping, "A source mapping must be provided");
			final UUID id = mapping.getId();
			checkArgument(!byId.containsKey(id), "Duplicate source mapping with id [%s]", id);
			final String name = mapping.getName();
			checkArgument(!byName.containsKey(name), "Duplicate source mapping with name [%s]", name);
			byId.put(id, mapping);
			byName.put(name, mapping);
			return this;
		}

		/**
		 * Adds a collection of source mappings.
		 * @param mappings Mappings to add.
		 * @return This builder.
		 */
		public Builder add(Iterable<? extends SourceMapping> mappings) {
			if (mappings != null) {
				for (SourceMapping m : mappings) {
					add(m);
				}
			}
			return this;
		}

		/**
		 * @see com.google.common.base.Supplier#get()
		 */
		public SourceMappings get() {
			return new SourceMappings(this);
		}
	}

}
