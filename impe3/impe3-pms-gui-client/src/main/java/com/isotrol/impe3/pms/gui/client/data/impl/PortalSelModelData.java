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
package com.isotrol.impe3.pms.gui.client.data.impl;

import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.portal.PortalSelDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId;

/**
 * @author Andrei Cojocaru
 *
 */
public class PortalSelModelData extends	AbstractDTOModelDataWithStateAndId<PortalSelDTO> {

	/**
	 * <b>Name</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;
	
	/**
	 * <b>Description</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_DESCRIPTION = Constants.PROPERTY_DESCRIPTION;
	
	/**
	 * Public properties for this class instances.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(
			AbstractDTOModelDataWithStateAndId.PROPERTIES, PROPERTY_NAME, PROPERTY_DESCRIPTION);
	
	/**
	 * <br/>
	 * @param dto
	 */
	public PortalSelModelData(PortalSelDTO dto) {
		super(dto);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if (property.equals(PROPERTY_NAME)) {
			return getDTO().getName();
		} else if (property.equals(PROPERTY_DESCRIPTION)) {
			return getDTO().getDescription();
		}
		return super.doGet(property);
	}
	
	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId
	 * #doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		return super.doSet(property, value);
	}
	
	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId#getPropertyNames()
	 */
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}
	
}
