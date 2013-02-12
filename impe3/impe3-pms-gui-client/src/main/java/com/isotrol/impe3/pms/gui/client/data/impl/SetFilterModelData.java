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

import com.isotrol.impe3.pms.api.portal.SetFilterDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDescribedModelData;


/**
 * Model data for SetFilterDTO
 * @author Manuel Ruiz
 * 
 */
public class SetFilterModelData extends AbstractDescribedModelData<SetFilterDTO> {

	/**
	 * <b>TYPE</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_TYPE = "type";

	/**
	 * Public properties for this class.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(AbstractDescribedModelData.PROPERTIES, PROPERTY_TYPE);

	public SetFilterModelData(SetFilterDTO dto) {
		super(dto);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		SetFilterDTO setFilterDto = getDTO();
		if (property.equals(PROPERTY_TYPE)) {
			return setFilterDto.getType().toString();
		}
		return super.doGet(property);
	}
	
	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDescribedModelData#getPropertyNames()
	 */
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}
}
