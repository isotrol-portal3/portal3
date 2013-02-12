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

package com.isotrol.impe3.api.support;


import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Maps.filterKeys;
import static com.google.common.collect.Maps.filterValues;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.api.Principal;


/**
 * Basic implementation for principals.
 * @author Andres Rodriguez
 */
public class BasicPrincipal implements Principal, Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -2153664384602777595L;
	private final String username;
	private final String displayName;
	private final Set<String> roles;
	private final Map<String, String> properties;

	public BasicPrincipal(final String username, final String displayName, final Iterable<String> roles,
		final Map<String, String> properties) {
		this.username = username;
		this.displayName = displayName;
		if (roles == null) {
			this.roles = ImmutableSet.of();
		} else {
			this.roles = ImmutableSet.copyOf(Iterables.filter(roles, Predicates.notNull()));
		}
		if (properties == null) {
			this.properties = ImmutableMap.of();
		} else {
			this.properties = ImmutableMap.copyOf(filterKeys(filterValues(properties, notNull()), notNull()));
		}
	}

	/**
	 * Returns the principal's user name.
	 * @return The principal's user name.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the principal's display name.
	 * @return The principal's display name.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Returns the principal's roles.
	 * @return The principal's roles.
	 */
	public Set<String> getRoles() {
		return roles;
	}

	/**
	 * Returns the principal's properties.
	 * @return The principal's properties as an unmodifiable map. Never {@code null}.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
}
