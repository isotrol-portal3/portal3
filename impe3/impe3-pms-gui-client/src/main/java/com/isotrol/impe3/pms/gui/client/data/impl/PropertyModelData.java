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

import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.pms.api.PropertyDTO;

/**
 * ModelData for Portal Properties
 * 
 * @author Andrei Cojocaru
 *
 */
public class PropertyModelData extends DTOModelData<PropertyDTO> {

	/**
	 * <b>Name</b> property descriptor
	 */
	public static final String PROPERTY_NAME = "name";
	
	/**
	 * <b>Value</b> property descriptor
	 */
	public static final String PROPERTY_VALUE = "value";
	
	/**
	 * Unique constructor
	 * @param dto wrapped Property DTO.
	 */
	public PropertyModelData(PropertyDTO dto) {
		super(dto);
	}
	
	/**
	 * Contains the public properties
	 */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_NAME, PROPERTY_VALUE);
	
	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if (property.equals(PROPERTY_NAME)) {
			return getDTO().getName();
		} else if (property.equals(PROPERTY_VALUE)){
			return getDTO().getValue();
		}
		throw new IllegalArgumentException(property + " is not readable for " + getClass().getName());
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		PropertyDTO dto = getDTO();
		Object oldValue = null;
		if (property.equals(PROPERTY_NAME)) {
			oldValue = dto.getName();
			dto.setName((String) value);
		} else if (property.equals(PROPERTY_VALUE)) {
			oldValue = dto.getValue();
			dto.setValue((String) value);
		} else {
			throw new IllegalArgumentException(property + " not writable for " + getClass().getName());			
		}
		return oldValue;
	}

	/** (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
