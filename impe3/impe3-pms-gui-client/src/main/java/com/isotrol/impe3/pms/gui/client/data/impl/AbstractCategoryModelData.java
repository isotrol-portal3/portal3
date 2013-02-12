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

import com.isotrol.impe3.pms.api.category.AbstractCategoryDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId;

/**
 * @author Andrei Cojocaru
 *
 * @param <D> type variable for the category DTO.
 */
public class AbstractCategoryModelData<D extends AbstractCategoryDTO> 
	extends AbstractDTOModelDataWithStateAndId<D> {

	/**
	 * <b>Routable</b> property descriptor.<br/>
	 */
	private static final String PROPERTY_ROUTABLE = "routable";
	/**
	 * <b>Visible</b> property descriptor.<br/>
	 */
	private static final String PROPERTY_VISIBLE = "visible";
	
	/**
	 * Public properties set for this class.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(
			AbstractDTOModelDataWithStateAndId.PROPERTIES,
			PROPERTY_ROUTABLE, PROPERTY_VISIBLE);
	
	/**
	 * <br/>
	 * @param dto
	 */
	public AbstractCategoryModelData(D dto) {
		super(dto);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if(property.equals(PROPERTY_ROUTABLE)) {
			return getDTO().isRoutable();
		} else if (property.equals(PROPERTY_VISIBLE)) {
			return getDTO().isVisible();
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
		if(property.equals(PROPERTY_ROUTABLE)) {
			D dto = getDTO();
			Boolean old = Boolean.valueOf(dto.isRoutable());
			dto.setRoutable((Boolean) value);
			return old;
		} else if (property.equals(PROPERTY_VISIBLE)) {
			D dto = getDTO();
			Boolean old = Boolean.valueOf(dto.isVisible());
			dto.setVisible((Boolean) value);
			return old;
		}
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
