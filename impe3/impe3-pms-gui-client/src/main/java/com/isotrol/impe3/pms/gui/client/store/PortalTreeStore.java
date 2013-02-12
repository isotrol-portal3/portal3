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

import java.util.List;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.isotrol.impe3.pms.api.portal.PortalTreeDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.PortalSelModelData;

/**
 * Tree store for a portal tree.
 * 
 * @author Andrei Cojocaru
 *
 */
public class PortalTreeStore extends TreeStore<PortalSelModelData> {
	
	/**
	 * Default constructor.<br/>
	 */
	public PortalTreeStore() {}
	
	/**
	 * Constructor with the passed root DTO.<br/>
	 */
	public PortalTreeStore(PortalTreeDTO rootDto) {
		add(rootDto, true);
	}
	
	/**
	 * Adds the passed DTO to the root of the tree.<br/>
	 * @param rootDto element to add to the root of the tree.
	 * @param addChildren if <code>true</code>, recursively adds the DTO children to the tree.
	 */
	public final void add(PortalTreeDTO rootDto, boolean addChildren) {
		PortalSelModelData rootModelData = new PortalSelModelData(rootDto.getNode());
		add(rootModelData, false);
		if(addChildren) {
			addChildren(rootModelData, rootDto);
		}
	}

	/**
	 * Adds the children of passed DTO as children for the passed ModelData.<br/>
	 * @param rootModelData ModelData
	 * @param rootDto DTO for the passed ModelData
	 */
	private void addChildren(PortalSelModelData rootModelData, PortalTreeDTO rootDto) {
		List<PortalTreeDTO> children = rootDto.getChildren();
		if(children != null && !children.isEmpty()) {
			for(PortalTreeDTO childDto : children) {
				PortalSelModelData childModelData = new PortalSelModelData(childDto.getNode());
				add(rootModelData, childModelData, false);
				addChildren(childModelData, childDto);
			}
		}
	}
}
