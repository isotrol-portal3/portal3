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
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.portal.PortalConfigurationSelDTO;

public class PortalConfigurationInstanceSelModelData extends DTOModelData<PortalConfigurationSelDTO> {
	
	
	
	
	
	/**
	 * <br/>
	 * @param dto
	 */
	public PortalConfigurationInstanceSelModelData(PortalConfigurationSelDTO dto) {
		super(dto);
	}
	
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;
	
	public static final String PROPERTY_DESCRIPTION = Constants.PROPERTY_DESCRIPTION;
	
	public static final String PROPERTY_VALIDITY = Constants.PROPERTY_VALIDITY;
	
	public static final String PROPERTY_HERENCY =  Constants.PROPERTY_HERENCY;
	
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_NAME,PROPERTY_DESCRIPTION,
			PROPERTY_VALIDITY,PROPERTY_HERENCY);
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

	@Override
	protected Object doGet(String property) {
		Object res = null;
		PortalConfigurationSelDTO config = getDTO();

		if (property.equals(PROPERTY_DESCRIPTION)) {
			res = config.getDescription();
		} else if (property.equals(PROPERTY_NAME)) {
			res = config.getName();
		} else if (property.equals(PROPERTY_VALIDITY)) {
			res = config.isValidity();
		} else if (property.equals(PROPERTY_HERENCY)) {
			res = config.getInherited();
		} else {
			throw new IllegalArgumentException("Property not readable: " + property);
		}

		return res;
		
	}

	@Override
	protected Object doSet(String property, Object value) {
		throw new IllegalArgumentException("Property not writable: " + property);
	}


}
