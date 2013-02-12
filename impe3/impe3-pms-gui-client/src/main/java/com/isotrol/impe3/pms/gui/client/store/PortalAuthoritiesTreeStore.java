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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.store;

import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.pms.api.user.Granted;
import com.isotrol.impe3.pms.api.user.PortalAuthorityDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.AuthModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.GrantedAuthModelData;

/**
 * 2-level tree store with all authorities at level 1, and their implied authorities as children.
 *
 * @author Andrei Cojocaru
 *
 */
public class PortalAuthoritiesTreeStore extends TreeStore<DTOModelData<?>> {

	/**
	 * Default constructor.
	 */
	public PortalAuthoritiesTreeStore() {}
	
	/**
	 * Populates the store with the passed granted authorities.	
	 * @param grantedAuthorities list of all authorities.
	 */
	public PortalAuthoritiesTreeStore(List<Granted<PortalAuthorityDTO>> grantedAuthorities) {
		load(grantedAuthorities);
	}

	/**
	 * Loads the passed authorities into the store.<br/>
	 * @param grantedAuthorities
	 */
	public final void load(List<Granted<PortalAuthorityDTO>> grantedAuthorities) {
		for (Granted<PortalAuthorityDTO> grantedAuthority : grantedAuthorities) {
			DTOModelData<?> model = new GrantedAuthModelData<PortalAuthorityDTO>(grantedAuthority);
			add(model, false);
			List<DTOModelData<?>> children = new LinkedList<DTOModelData<?>>();
			for (PortalAuthorityDTO auth : grantedAuthority.get().getImplied()) {
				children.add(new AuthModelData<PortalAuthorityDTO>(auth));
			}
			add(model, children, false);
		}
	}
	
}
