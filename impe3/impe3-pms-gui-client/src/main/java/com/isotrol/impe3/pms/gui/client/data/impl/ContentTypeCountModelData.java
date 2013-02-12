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
import com.isotrol.impe3.pms.api.esvc.nr.ContentTypeCountDTO;

/**
 * ModelData for the {@link ContentTypeCountDTO} type.
 * @author Andrei Cojocaru
 *
 */
public class ContentTypeCountModelData extends DTOModelData<ContentTypeCountDTO> {

	/**
	 * The name of the Content Type.<br/>
	 */
	public static final String PROPERTY_CT_NAME = "ct-name";
	
	/**
	 * The count of Content Type instances found.<br/>
	 */
	public static final String PROPERTY_COUNT = "count";
	
	/**
	 * Public properties.<br/>
	 */
	private static final Set<String> PROPERTIES = propertySet(PROPERTY_CT_NAME, PROPERTY_COUNT);

	/**
	 * @param dto
	 */
	public ContentTypeCountModelData(ContentTypeCountDTO dto) {
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
		String res = null;
		if (property.equals(PROPERTY_CT_NAME)) {
			res = getDTO().getContentType().getName();
		} else if (property.equals(PROPERTY_COUNT)) {
			res = Integer.toString(getDTO().getCount());
		} else {
			throw new IllegalArgumentException(getClass().getName() + ": not readable property:" + property);
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
		throw new IllegalArgumentException(getClass().getName() + ": not writeable property:" + property);
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
