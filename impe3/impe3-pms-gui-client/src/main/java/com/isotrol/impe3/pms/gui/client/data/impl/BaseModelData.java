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
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AInheritedModelData;
import com.isotrol.impe3.pms.gui.client.util.Labels;

/**
 * ModelData for BaseDTO objects.
 * 
 * @author Andrei Cojocaru
 */
public class BaseModelData extends DTOModelData<BaseDTO> {

	/**
	 * <b>Key</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_KEY = Constants.PROPERTY_KEY;
	
	/**
	 * <b>URI</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_URI = Constants.PROPERTY_URI;
	
	/**
	 * Public properties for this class.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(AInheritedModelData.PROPERTIES,
															PROPERTY_KEY,PROPERTY_URI);
	
	/**
	 * Constructor provided with dto.<br/>
	 * @param dto
	 */
	public BaseModelData(BaseDTO dto) {
		super(dto);
	}
	
	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		Object res = null;
		BaseDTO baseDto = getDTO();
		
		if (property.equals(PROPERTY_KEY)) {
			res = baseDto.getKey();
		} else if (property.equals(PROPERTY_URI)) {
			res = baseDto.getUri();
		} else {
			throw new IllegalArgumentException(Labels.PROPERTY_NOT_READABLE + property);
		}
		
		return res;
	}
	
	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		Object old = null;
		BaseDTO baseDto = getDTO();
		
		if (property.equals(PROPERTY_KEY)) {
			old = baseDto.getKey();
			baseDto.setKey((String) value);
		} else if (property.equals(PROPERTY_URI)) {
			old = baseDto.getUri();
			baseDto.setUri((String) value);
		} else {
			throw new IllegalArgumentException(Labels.PROPERTY_NOT_WRITABLE + property);
		}
		return old;
	}
	
	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
