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

package com.isotrol.impe3.pms.core.impl;


import static com.google.common.collect.Maps.transformValues;
import static com.google.common.collect.Sets.newEnumSet;
import static com.google.common.collect.Sets.newHashSet;
import static com.isotrol.impe3.pms.api.GlobalAuthorityMap.flatten;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.PortalAuthorityMap;
import com.isotrol.impe3.pms.api.user.AuthorizationDTO;
import com.isotrol.impe3.pms.core.obj.PortalsObject;
import com.isotrol.impe3.pms.model.PortalAuthorityValue;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Authorization object. The object Id is the current user's id.
 * @author Andres Rodriguez
 */
public final class Authorization extends AbstractIdentifiable {
	private static final Function<EnumSet<PortalAuthority>, EnumSet<PortalAuthority>> FLATTEN = new Function<EnumSet<PortalAuthority>, EnumSet<PortalAuthority>>() {
		public EnumSet<PortalAuthority> apply(EnumSet<PortalAuthority> from) {
			return PortalAuthorityMap.flatten(from);
		}
	};

	/** Whether the user is root. */
	private final boolean root;
	/** Global authorities. */
	private final EnumSet<GlobalAuthority> global;
	/** Portal authorities. */
	private final ImmutableMap<UUID, EnumSet<PortalAuthority>> portal;

	/**
	 * Constructor.
	 * @param portals Portals.
	 * @param user Logged user.
	 */
	Authorization(PortalsObject portals, UserEntity user) {
		super(user.getId());
		this.root = user.isRoot();
		this.global = newEnumSet(flatten(user.getGlobalRoles(), user.getGlobalAuthorities()), GlobalAuthority.class);
		if (!user.getPortalAuthorities().isEmpty()) {
			final Map<UUID, EnumSet<PortalAuthority>> portalMap = Maps.newHashMap();
			for (PortalAuthorityValue pav : user.getPortalAuthorities()) {
				final UUID pid = pav.getPortal().getId();
				if (portals.containsKey(pid)) {
					EnumSet<PortalAuthority> set = portalMap.get(pid);
					if (set == null) {
						set = EnumSet.of(pav.getPortalAuthority());
						portalMap.put(pid, set);
					} else {
						set.add(pav.getPortalAuthority());
					}
				}
			}
			if (!portalMap.isEmpty()) {
				portal = ImmutableMap.copyOf(transformValues(portalMap, FLATTEN));
				global.add(GlobalAuthority.PORTAL_TREE);
			} else {
				portal = ImmutableMap.of();
			}
		} else {
			portal = ImmutableMap.of();
		}
	}

	/**
	 * Returns whether the user is root.
	 * @return True if the user is root.
	 */
	public boolean isRoot() {
		return root;
	}

	/**
	 * Returns whether the user has a global authority granted.
	 * @param ga Global authority to check.
	 * @return True if the user has the authority granted.
	 */
	public boolean hasGlobal(GlobalAuthority ga) {
		return global.contains(ga);
	}

	/**
	 * Returns whether the user has a collection of global authorities granted.
	 * @param gas Global authorities to check.
	 * @return True if the user has the authorities granted.
	 */
	public boolean hasGlobal(Collection<GlobalAuthority> gas) {
		return global.containsAll(gas);
	}
	
	/**
	 * Returns whether the user has a portal authority granted.
	 * @param id Portal Id.
	 * @param pa Portal authority to check.
	 * @return True if the user has the authority granted.
	 */
	public boolean hasPortal(UUID id, PortalAuthority pa) {
		return portal.containsKey(id) && portal.get(id).contains(pa);
	}

	/**
	 * Returns whether the user has set of portal authorities granted.
	 * @param id Portal Id.
	 * @param pas Portal authorities to check.
	 * @return True if the user has the authorities granted.
	 */
	public boolean hasPortal(UUID id, Collection<PortalAuthority> pas) {
		return portal.containsKey(id) && portal.get(id).containsAll(pas);
	}

	public AuthorizationDTO toDTO() {
		final AuthorizationDTO dto = new AuthorizationDTO();
		dto.setRoot(root);
		dto.setAuthorities(newHashSet(global));
		return dto;
	}

}
