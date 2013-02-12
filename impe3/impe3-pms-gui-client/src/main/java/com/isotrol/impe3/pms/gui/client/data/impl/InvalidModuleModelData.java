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

import com.isotrol.impe3.pms.api.mreg.InvalidModuleDTO;


/**
 * Model for invalid modules
 * @author Manuel Ruiz
 * 
 */
public class InvalidModuleModelData extends AbstractModuleModelData<InvalidModuleDTO> {

	/** invalid module property error */
	public static final String PROPERTY_ERROR = "error";
	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(AbstractModuleModelData.PROPERTIES, PROPERTY_ERROR);

	/**
	 * Constructor
	 * @param dto
	 */
	public InvalidModuleModelData(InvalidModuleDTO dto) {
		super(dto);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.impl.AbstractModuleModelData#doGet(java.lang.String)
	 */
	/**
	 * <br/>
	 */
	protected Object doGet(String property) {
		if (PROPERTY_ERROR.equals(property)) {
			return getDTO().getError();
		}
		return super.doGet(property);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.impl.AbstractModuleModelData#doSet(java.lang.String, java.lang.Object)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected Object doSet(String property, Object value) {
		throw new IllegalArgumentException();
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.impl.AbstractModuleModelData#getPropertyNames()
	 */
	/**
	 * <br/>
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
