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
 * Routing domains collection.
 * @author Andres Rodriguez
 */
public final class RoutingDomains extends ForwardingMap<UUID, RoutingDomain> {
	/** By Id. */
	private final ImmutableMap<UUID, RoutingDomain> byId;
	/** By name. */
	private final ImmutableMap<String, RoutingDomain> byName;

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
	private RoutingDomains(Builder builder) {
		this.byId = ImmutableMap.copyOf(builder.byId);
		this.byName = ImmutableMap.copyOf(builder.byName);
	}

	@Override
	protected Map<UUID, RoutingDomain> delegate() {
		return byId;
	}

	/**
	 * Returns a routing domain by name.
	 * @param name Requested name.
	 * @return The routing domain with the requested name or {@code null} if not found.
	 */
	public RoutingDomain getByName(String name) {
		return byName.get(name);
	}

	/**
	 * Builder for RoutingDomains.
	 * @author Andres Rodriguez
	 */
	public static final class Builder implements com.isotrol.impe3.api.Builder<RoutingDomains> {
		/** By Id. */
		private final Map<UUID, RoutingDomain> byId = Maps.newHashMap();
		/** By name. */
		private final Map<String, RoutingDomain> byName = Maps.newHashMap();

		/** Constructor. */
		private Builder() {
		}

		/**
		 * Adds a routing domain.
		 * @param domain Routing domain to add.
		 * @return This builder.
		 */
		public Builder add(RoutingDomain domain) {
			checkNotNull(domain, "A routing domain must be provided");
			final UUID id = domain.getId();
			checkArgument(!byId.containsKey(id), "Duplicate routing domain with id [%s]", id);
			final String name = domain.getName();
			checkArgument(!byName.containsKey(name), "Duplicate routing domain with name [%s]", name);
			byId.put(id, domain);
			byName.put(name, domain);
			return this;
		}

		/**
		 * Adds a collection of routing domains.
		 * @param domains Routing domains to add.
		 * @return This builder.
		 */
		public Builder add(Iterable<? extends RoutingDomain> domains) {
			if (domains != null) {
				for (RoutingDomain d : domains) {
					add(d);
				}
			}
			return this;
		}

		/**
		 * @see com.google.common.base.Supplier#get()
		 */
		public RoutingDomains get() {
			return new RoutingDomains(this);
		}
	}

}
