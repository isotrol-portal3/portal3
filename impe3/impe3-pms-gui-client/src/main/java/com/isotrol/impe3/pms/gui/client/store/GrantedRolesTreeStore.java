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
import com.isotrol.impe3.pms.api.user.GlobalRoleDTO;
import com.isotrol.impe3.pms.api.user.Granted;
import com.isotrol.impe3.pms.gui.client.data.impl.AuthModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.GrantedAuthModelData;

/**
 * 2-level tree store with Roles at level one, and the set of authorities 
 * wrapped by each Role as children.
 * @author Andrei Cojocaru
 *
 */
public class GrantedRolesTreeStore extends TreeStore<DTOModelData<?>> {

	/**
	 * Default constructor.
	 */
	public GrantedRolesTreeStore() {}
	
	/**
	 * Constructor provided with granted roles list.
	 */
	public GrantedRolesTreeStore(List<Granted<GlobalRoleDTO>> roles) {
		load(roles);
	}

	/**
	 * <br/>
	 * @param roles
	 */
	public final void load(List<Granted<GlobalRoleDTO>> roles) {
		for (Granted<GlobalRoleDTO> role : roles) {
			GrantedAuthModelData<GlobalRoleDTO> model = 
				new GrantedAuthModelData<GlobalRoleDTO>(role);
			add(model,false);
			List<DTOModelData<?>> children = new LinkedList<DTOModelData<?>>();
			for (GlobalAuthorityDTO auth : role.get().getImplied()) {
				children.add(new AuthModelData<GlobalAuthorityDTO>(auth));
			}
			add(model, children, false);
		}
	}
	
}
