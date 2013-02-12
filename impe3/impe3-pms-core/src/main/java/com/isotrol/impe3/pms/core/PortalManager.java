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

package com.isotrol.impe3.pms.core;


import java.util.UUID;

import net.sf.derquinsej.collect.Hierarchy;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.obj.PortalsObject;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEntity;


/**
 * Portal manager.
 * @author Andres Rodriguez.
 */
public interface PortalManager {
	/** Request parameter to mask errors. */
	String MASK_ERROR = "impe3MaskError";
	/**
	 * Touches the portal hierarchy, creating a new definition of the modified portal if needed.
	 * @param portals Portals hierarchy.
	 * @param entity Portal entity to touch.
	 * @return Updatable portal definition.
	 */
	PortalDfn touchHierarchy(PortalsObject portals, PortalEntity entity) throws PMSException;

	/**
	 * Touches a portal entity, creating a new definition if needed.
	 * @param entity Portal entity to touch.
	 * @return Updatable portal definition.
	 */
	PortalDfn touchOffline(PortalEntity entity) throws PMSException;

	/**
	 * Touches a portal entity's components, creating a new definition if needed.
	 * @param portals Portals hierarchy.
	 * @param entity Portal entity to touch.
	 * @return Updatable portal definition.
	 */
	PortalDfn touchComponents(PortalsObject portals, PortalEntity entity) throws PMSException;

	/**
	 * Touches a portal entity's pages, creating a new definition if needed.
	 * @param portals Portals hierarchy.
	 * @param entity Portal entity to touch.
	 * @return Updatable portal definition.
	 */
	PortalDfn touchPages(PortalsObject portals, PortalEntity entity) throws PMSException;

	/**
	 * Returns the categories of the portal.
	 * @param dfn Portal definition.
	 * @return The categories of the portal.
	 */
	Hierarchy<UUID, CategoryEntity> getCategories(PortalDfn dfn);
}
