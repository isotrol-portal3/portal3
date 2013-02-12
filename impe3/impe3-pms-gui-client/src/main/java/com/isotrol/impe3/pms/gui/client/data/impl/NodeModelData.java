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
import com.isotrol.impe3.pms.api.esvc.nr.NodeDTO;

/**
 * ModelDta for NodeModel DTOs.
 * @author Andrei Cojocaru
 *
 */
public class NodeModelData extends DTOModelData<NodeDTO> {

	/**
	 * <b>ID</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_ID = "id";
	
	/**
	 * <b>date</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_DATE = "date";
	
	/**
	 * <b>description</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_DESCRIPTION = "desc";
	
	/**
	 * <b>title</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_TITLE = "title";
	
	/**
	 * Public properties.<br/>
	 */
	private static final Set<String> PROPERTIES = propertySet(
			PROPERTY_ID, PROPERTY_DATE, PROPERTY_DESCRIPTION, PROPERTY_TITLE);
	
	/**
	 * Constructor provided with bound DTO.
	 * @param dto
	 */
	public NodeModelData(NodeDTO dto) {
		super(dto);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected Object doGet(String property) {
		NodeDTO dto = getDTO();
		Object res = null;
		if (property.equals(PROPERTY_DATE)) {
			res = dto.getDate();
		} else if (property.equals(PROPERTY_DESCRIPTION)) {
			res = dto.getDescription();
		} else if (property.equals(PROPERTY_TITLE)) {
			res = dto.getTitle();
		} else if (property.equals(PROPERTY_ID)) {
			res = dto.getId();
		} else {
			throw new IllegalArgumentException(getClass().getName() + "#doGet(): property not readable: " + property);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected Object doSet(String property, Object value) {
		throw new IllegalStateException(getClass().getName() + "#doSet(): property not writable: " + property);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	/**
	 * <br/>
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}
	
}
