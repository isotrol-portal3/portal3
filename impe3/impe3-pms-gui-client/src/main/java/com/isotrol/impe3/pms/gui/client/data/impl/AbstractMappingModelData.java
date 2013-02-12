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


import java.util.Set;

import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.pms.api.smap.AbstractMappingDTO;


/**
 * Abstract model for a mapping item
 * @author Manuel Ruiz
 * @param <D>
 * 
 */
public abstract class AbstractMappingModelData<D extends AbstractMappingDTO> extends DTOModelData<D> {

	/** descriptor for mapping property */
	public static final String PROPERTY_MAPPING = "mapping";
	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_MAPPING);

	/**
	 * Default constructor
	 */
	public AbstractMappingModelData(D dto) {
		super(dto);
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if (PROPERTY_MAPPING.equals(property)) {
			return getDTO().getMapping();
		}
		throw new IllegalArgumentException();
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		final D dto = getDTO();
		if (PROPERTY_MAPPING.equals(property)) {
			final String old = dto.getMapping();
			dto.setMapping((String) value);
			return old;
		}
		throw new IllegalArgumentException();
	}
}
