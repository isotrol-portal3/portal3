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
import com.isotrol.impe3.pms.api.user.GlobalAuthorityDTO;
import com.isotrol.impe3.pms.api.user.Granted;
import com.isotrol.impe3.pms.gui.client.data.impl.AuthModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.GrantedAuthModelData;

/**
 * 2-level tree store with all authorities at level 1, and their implied
 * authorities as children.
 * @author Andrei Cojocaru
 *
 */
public class GrantedAuthoritiesTreeStore extends TreeStore<DTOModelData<?>> {

	/**
	 * No args constructor.
	 */
	public GrantedAuthoritiesTreeStore() {}
	
	/**
	 * Populates the store withe the passed granted authorities.
	 * @param grantedAuthorities
	 */
	public GrantedAuthoritiesTreeStore(List<Granted<GlobalAuthorityDTO>> grantedAuthorities) {
		load(grantedAuthorities);
	}

	/**
	 * Loads the passed authorities into the store. Does not remove the exising ones.<br/>
	 * @param grantedAuthorities
	 */
	public final void load(List<Granted<GlobalAuthorityDTO>> grantedAuthorities) {
		for (Granted<GlobalAuthorityDTO> grantedAuthority : grantedAuthorities) {
			DTOModelData<?> model = 
				new GrantedAuthModelData<GlobalAuthorityDTO>(grantedAuthority);
			add(model, false);
			// add children:
			List<DTOModelData<?>> children = new LinkedList<DTOModelData<?>>();
			for (GlobalAuthorityDTO auth : grantedAuthority.get().getImplied()) {
				children.add(new AuthModelData<GlobalAuthorityDTO>(auth));
			}
			add(model, children, false);
		}
	}
	
}
