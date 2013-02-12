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

import com.google.common.base.Preconditions;


/**
 * Value that represents a routing domain.
 * @author Andres Rodriguez
 */
public final class RoutingDomain extends AbstractIdentifiable {
	/** Null name error message. */
	private static final String NAME_ERROR = "A routing domain name must be provided.";
	/** Offline base error message. */
	private static final String OFFLINE_ERROR = "An offline base name must be provided.";
	/** Online base error message. */
	private static final String ONLINE_ERROR = "An offline base name must be provided.";

	/**
	 * Returns a new builder.
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/** Routing domain name. */
	private final String name;
	/** Routing domain description. */
	private final String description;
	/** Offline base. */
	private final RoutingBase offline;
	/** Online base. */
	private final RoutingBase online;

	/**
	 * Constructor.
	 * @param builder Builder.
	 */
	private RoutingDomain(Builder builder) {
		super(builder);
		this.name = builder.name;
		this.description = builder.description;
		this.offline = builder.offline;
		this.online = builder.online;
	}

	/**
	 * Returns the routing domain name.
	 * @return The routing domain name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the routing domain description.
	 * @return The routing domain description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the offline base.
	 * @return The offline base.
	 */
	public RoutingBase getOffline() {
		return offline;
	}

	/**
	 * Returns the online base.
	 * @return The online base.
	 */
	public RoutingBase getOnline() {
		return online;
	}

	/**
	 * Builder for SourceMapping.
	 * @author Andres Rodriguez
	 */
	public static final class Builder extends AbstractIdentifiable.Builder<Builder, RoutingDomain> {
		/** Mapping name. */
		private String name;
		/** Mapping description. */
		private String description;
		/** Offline base. */
		private RoutingBase offline;
		/** Online base. */
		private RoutingBase online;

		/** Constructor. */
		private Builder() {
		}

		/**
		 * Sets the login name.
		 * @param name The login name.
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
		 * Sets the offline base.
		 * @param base The offline base.
		 * @return This builder.
		 */
		public Builder setOffline(RoutingBase base) {
			this.offline = checkNotNull(base, OFFLINE_ERROR);
			return this;
		}

		/**
		 * Sets the online base.
		 * @param base The offline base.
		 * @return This builder.
		 */
		public Builder setOnline(RoutingBase base) {
			this.online = checkNotNull(base, ONLINE_ERROR);
			return this;
		}

		@Override
		public void checkState() {
			super.checkState();
			Preconditions.checkState(name != null, NAME_ERROR);
			Preconditions.checkState(offline != null, OFFLINE_ERROR);
			Preconditions.checkState(online != null, ONLINE_ERROR);
		}

		/**
		 * Returns the built source mapping.
		 * @return The built source mapping.
		 * @throws IllegalStateException in case of an error.
		 */
		protected RoutingDomain doGet() {
			return new RoutingDomain(this);
		}

	}

}
