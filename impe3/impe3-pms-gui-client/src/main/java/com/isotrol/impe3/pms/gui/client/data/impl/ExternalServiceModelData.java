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

import com.isotrol.impe3.pms.api.esvc.ExternalServiceDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDescribedModelData;

/**
 * ModelData for external services.
 * 
 * @author Manuel Ruiz
 *
 */
public class ExternalServiceModelData extends AbstractDescribedModelData<ExternalServiceDTO> {

	/**
	 * <b>ID</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_ID = "id";
	
	/**
	 * Public properties.<br/>
	 */
	public static final Set<String> PROPERTIES = 
		propertySet(AbstractDescribedModelData.PROPERTIES, PROPERTY_ID);

	/**
	 * ModelData constructor provided with bound DTO.<br/>
	 * @param dto
	 */
	public ExternalServiceModelData(ExternalServiceDTO dto) {
		super(dto);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDescribedModelData#doGet(java.lang.String)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected Object doGet(String property) {
		if (property.equals(PROPERTY_ID)) {
			return getDTO().getId();
		}
		return super.doGet(property);
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDescribedModelData#getPropertyNames()
	 */
	/**
	 * <br/>
	 */
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
