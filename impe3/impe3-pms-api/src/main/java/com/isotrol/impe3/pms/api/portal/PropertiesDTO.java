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

package com.isotrol.impe3.pms.api.portal;


import java.io.Serializable;
import java.util.List;

import com.isotrol.impe3.pms.api.PropertyDTO;


/**
 * DTO representing the set of portal properties.
 * @author Andres Rodriguez
 */
public final class PropertiesDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 2419178739583113746L;

	/** Inherited properties. */
	private List<PropertyDTO> inherited;
	/** Own properties. */
	private List<PropertyDTO> properties;

	/** Default constructor. */
	public PropertiesDTO() {
	}
	
	/**
	 * Returns the inherited properties.
	 * @return The inherited properties.
	 */
	public List<PropertyDTO> getInherited() {
		return inherited;
	}
	
	/**
	 * Sets the inherited properties.
	 * @param inherited The inherited properties.
	 */
	public void setInherited(List<PropertyDTO> inherited) {
		this.inherited = inherited;
	}
	
	/**
	 * Returns the portal's own properties.
	 * @return The portal's own properties.
	 */
	public List<PropertyDTO> getProperties() {
		return properties;
	}
	
	/**
	 * Sets the portal's own properties.
	 * @param properties The portal's own properties.
	 */
	public void setProperties(List<PropertyDTO> properties) {
		this.properties = properties;
	}
}
