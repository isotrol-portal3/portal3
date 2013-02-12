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


import static com.isotrol.impe3.pms.api.PortalAuthority.BASE;
import static com.isotrol.impe3.pms.api.PortalAuthority.COMPONENT;
import static com.isotrol.impe3.pms.api.PortalAuthority.GET;
import static com.isotrol.impe3.pms.api.PortalAuthority.PAGE;
import static com.isotrol.impe3.pms.api.PortalAuthority.PROPERTY;
import static com.isotrol.impe3.pms.api.PortalAuthority.PUBLISH;
import static com.isotrol.impe3.pms.api.PortalAuthority.SET;
import static java.util.Collections.unmodifiableSet;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;


/**
 * Portal authorities hierarchy.
 * @author Andres Rodriguez
 */
public final class PortalAuthorityMap {
	/** Not instantiable. */
	private PortalAuthorityMap() {
		throw new AssertionError();
	}

	private static final Set<PortalAuthority> set(PortalAuthority... pas) {
		final Set<PortalAuthority> s = EnumSet.noneOf(PortalAuthority.class);
		for (PortalAuthority pa : pas) {
			s.add(pa);
		}
		return unmodifiableSet(s);
	}

	private static final EnumMap<PortalAuthority, Set<PortalAuthority>> MAP;

	static {
		MAP = new EnumMap<PortalAuthority, Set<PortalAuthority>>(PortalAuthority.class);
		MAP.put(BASE, set(GET));
		MAP.put(PROPERTY, set(GET));
		MAP.put(COMPONENT, set(GET));
		MAP.put(PAGE, set(GET));
		MAP.put(SET, set(BASE, PROPERTY, COMPONENT, PAGE));
		MAP.put(PUBLISH, set(GET));
	}

	public static Set<PortalAuthority> getImplied(PortalAuthority ga) {
		Set<PortalAuthority> set = MAP.get(ga);
		if (set == null) {
			return Collections.emptySet();
		}
		return set;
	}

	private static void add(EnumSet<PortalAuthority> set, Iterable<PortalAuthority> pas) {
		for (PortalAuthority pa : pas) {
			if (!set.contains(pa)) {
				set.add(pa);
				add(set, getImplied(pa));
			}
		}
	}

	/**
	 * Flatten a collection of authorities.
	 * @param c Collection to flatten.
	 * @return The flattened collection.
	 */
	public static EnumSet<PortalAuthority> flatten(Collection<PortalAuthority> c) {
		final EnumSet<PortalAuthority> set = EnumSet.noneOf(PortalAuthority.class);
		if (c != null && !c.isEmpty()) {
			add(set, c);
		}
		return set;
	}

}
