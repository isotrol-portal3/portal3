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

package com.isotrol.impe3.pms.api;


import static com.isotrol.impe3.pms.api.GlobalAuthority.CATEGORY_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.CATEGORY_SET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.CONNECTOR_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.CONNECTOR_SET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.DEVICE_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.DEVICE_SET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.EDITION_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.EDITION_PUBLISH;
import static com.isotrol.impe3.pms.api.GlobalAuthority.EDITION_REPUBLISH;
import static com.isotrol.impe3.pms.api.GlobalAuthority.ENV_CONFIG_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.ENV_CONFIG_SET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.PORTAL_BASE;
import static com.isotrol.impe3.pms.api.GlobalAuthority.PORTAL_COMPONENT;
import static com.isotrol.impe3.pms.api.GlobalAuthority.PORTAL_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.PORTAL_PAGE;
import static com.isotrol.impe3.pms.api.GlobalAuthority.PORTAL_PROPERTY;
import static com.isotrol.impe3.pms.api.GlobalAuthority.PORTAL_SET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.PORTAL_TREE;
import static com.isotrol.impe3.pms.api.GlobalAuthority.RD_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.RD_SET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.SMAP_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.SMAP_SET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.TYPE_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.TYPE_SET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.USER_GET;
import static com.isotrol.impe3.pms.api.GlobalAuthority.USER_PWD;
import static com.isotrol.impe3.pms.api.GlobalAuthority.USER_SET;
import static java.util.Collections.unmodifiableSet;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;


/**
 * Global authorities hierarchy.
 * @author Andres Rodriguez
 */
public final class GlobalAuthorityMap {
	/** Not instantiable. */
	private GlobalAuthorityMap() {
		throw new AssertionError();
	}

	private static final Set<GlobalAuthority> set(GlobalAuthority... gas) {
		final Set<GlobalAuthority> s = EnumSet.noneOf(GlobalAuthority.class);
		for (GlobalAuthority ga : gas) {
			s.add(ga);
		}
		return unmodifiableSet(s);
	}

	private static final EnumMap<GlobalAuthority, Set<GlobalAuthority>> MAP;

	static {
		MAP = new EnumMap<GlobalAuthority, Set<GlobalAuthority>>(GlobalAuthority.class);
		MAP.put(TYPE_SET, set(TYPE_GET));
		MAP.put(CATEGORY_SET, set(CATEGORY_GET));
		MAP.put(SMAP_SET, set(SMAP_GET));
		MAP.put(CONNECTOR_SET, set(CONNECTOR_GET));
		MAP.put(PORTAL_GET, set(PORTAL_TREE));
		MAP.put(PORTAL_BASE, set(PORTAL_GET));
		MAP.put(PORTAL_PROPERTY, set(PORTAL_GET));
		MAP.put(PORTAL_COMPONENT, set(PORTAL_GET));
		MAP.put(PORTAL_PAGE, set(PORTAL_GET));
		MAP.put(PORTAL_SET, set(PORTAL_BASE, PORTAL_PROPERTY, PORTAL_COMPONENT, PORTAL_PAGE));
		MAP.put(USER_GET, set(PORTAL_TREE));
		MAP.put(USER_SET, set(USER_GET));
		MAP.put(USER_PWD, set(USER_GET));
		MAP.put(ENV_CONFIG_SET, set(ENV_CONFIG_GET));
		MAP.put(EDITION_PUBLISH, set(EDITION_GET));
		MAP.put(EDITION_REPUBLISH, set(EDITION_PUBLISH));
		MAP.put(RD_SET, set(RD_GET));
		MAP.put(DEVICE_SET, set(DEVICE_GET));
	}

	public static Set<GlobalAuthority> getImplied(GlobalAuthority ga) {
		Set<GlobalAuthority> set = MAP.get(ga);
		if (set == null) {
			return Collections.emptySet();
		}
		return set;
	}

	private static void add(EnumSet<GlobalAuthority> set, Iterable<GlobalAuthority> gas) {
		for (GlobalAuthority ga : gas) {
			if (!set.contains(ga)) {
				set.add(ga);
				add(set, getImplied(ga));
			}
		}
	}

	/**
	 * Flatten a collection of authorities.
	 * @param c Collection to flatten.
	 * @return The flattened collection.
	 */
	public static Set<GlobalAuthority> flatten(Collection<GlobalAuthority> c) {
		final EnumSet<GlobalAuthority> set = EnumSet.noneOf(GlobalAuthority.class);
		if (c != null && !c.isEmpty()) {
			add(set, c);
		}
		return set;
	}

	/**
	 * Joins and flattens a collection of roles and another of authorities.
	 * @param grs Roles.
	 * @param gas Global authorities.
	 * @return The flattened collection.
	 */
	public static Set<GlobalAuthority> flatten(Collection<GlobalRole> grs, Collection<GlobalAuthority> gas) {
		if (grs == null || grs.isEmpty()) {
			return flatten(gas);
		}
		final EnumSet<GlobalAuthority> set = EnumSet.noneOf(GlobalAuthority.class);
		for (GlobalRole gr : grs) {
			set.addAll(gr.getAuthorities());
		}
		if (gas != null) {
			set.addAll(gas);
		}
		return flatten(set);
	}

}
