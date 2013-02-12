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

import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;


/**
 * Model for Connectors Modules
 * @author Manuel Ruiz
 * 
 */
public class ConnectorModuleModelData extends AbstractValidModuleModelData<ConnectorModuleDTO> {

	/** Property to display (name - description) */
	public static final String PROPERTY_TO_DISPLAY = "name-desc";
	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(
			AbstractModuleModelData.PROPERTIES, PROPERTY_TO_DISPLAY);

	/**
	 * Constructor
	 * @param dto
	 */
	public ConnectorModuleModelData(ConnectorModuleDTO dto) {
		super(dto);
	}

	protected Object doGet(String property) {
		if(PROPERTY_TO_DISPLAY.equals(property)) {
			return getDTO().getName() + " - " + getDTO().getDescription();
		}
		return super.doGet(property);
	}

	@Override
	protected Object doSet(String property, Object value) {
		throw new IllegalArgumentException();
	}

	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
