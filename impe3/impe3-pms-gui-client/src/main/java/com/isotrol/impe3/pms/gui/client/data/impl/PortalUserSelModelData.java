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

package com.isotrol.impe3.pms.gui.client.data.impl;


import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.users.api.PortalUserSelDTO;


/**
 * ModelData for PortalUserSelDTO
 * 
 * @author Manuel Ruiz
 * 
 */
public class PortalUserSelModelData extends DTOModelData<PortalUserSelDTO> {

	/**
	 * Descriptor for user ID.<br/>
	 */
	public static final String PROPERTY_ID = "id";
	
	/** Descriptor for user name property */
	public static final String PROPERTY_NAME = "name";

	/** Descriptor for user name property */
	public static final String PROPERTY_DISPLAY_NAME = "displayName";

	/** Descriptor for user active property */
	public static final String PROPERTY_ACTIVE = "active";

	/**
	 * Public properties for this class instances.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(
			PROPERTY_ID, PROPERTY_NAME, PROPERTY_DISPLAY_NAME, PROPERTY_ACTIVE);

	/**
	 * <br/>
	 * @param dto
	 */
	public PortalUserSelModelData(PortalUserSelDTO dto) {
		super(dto);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.data.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if (PROPERTY_NAME.equals(property)) {
			return getDTO().getUsername();
		} else if (PROPERTY_DISPLAY_NAME.equals(property)) {
			return getDTO().getDisplayName();
		} else if (PROPERTY_ACTIVE.equals(property)) {
			return getDTO().isActive();
		} else if (PROPERTY_ID.equals(property)) {
			return getDTO().getId();
		}

		throw new IllegalArgumentException("Not readable property: " + property);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.data.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		// nothing to write here:
		throw new IllegalArgumentException("Not writable property: " + property);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
