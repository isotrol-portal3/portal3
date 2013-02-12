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

import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.smap.SourceMappingSelDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithId;


/**
 * Model for source mappings
 * @author Manuel Ruiz
 * 
 */
public class SourceMappingSelModelData extends AbstractDTOModelDataWithId<SourceMappingSelDTO> {

	/** descriptor for source mapping selector property name */
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;

	/** descriptor for source mapping selector property description */
	public static final String PROPERTY_DESCRIPTION = Constants.PROPERTY_DESCRIPTION;

	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(
			AbstractDTOModelDataWithId.PROPERTIES, PROPERTY_NAME, PROPERTY_DESCRIPTION);

	/**
	 * Default constructor
	 */
	public SourceMappingSelModelData(SourceMappingSelDTO dto) {
		super(dto);
	}

	/**
	 * Default constructor
	 */
	public SourceMappingSelModelData() {
		this(new SourceMappingSelDTO());
	}

	@Override
	protected Object doGet(String property) {
		if (PROPERTY_NAME.equals(property)) {
			return getDTO().getName();
		} else if (PROPERTY_DESCRIPTION.equals(property)) {
			return getDTO().getDescription();
		}
		return super.doGet(property);
	}
	
	@Override
	protected Object doSet(String property, Object value) {
		// no properties should be set here
		return super.doSet(property, value);
	}

	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
