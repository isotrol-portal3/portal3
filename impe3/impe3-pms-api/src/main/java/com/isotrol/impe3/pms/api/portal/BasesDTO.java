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


/**
 * DTO representing the set of portal bases.
 * @author Andres Rodriguez
 */
public final class BasesDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -2533432381148184651L;
	/** Inherited bases. */
	private List<BaseDTO> inherited;
	/** Own bases. */
	private List<BaseDTO> bases;

	/** Default constructor. */
	public BasesDTO() {
	}
	
	/**
	 * Returns the inherited bases.
	 * @return The inherited bases.
	 */
	public List<BaseDTO> getInherited() {
		return inherited;
	}
	
	/**
	 * Sets the inherited bases.
	 * @param inherited The inherited bases.
	 */
	public void setInherited(List<BaseDTO> inherited) {
		this.inherited = inherited;
	}
	
	/**
	 * Returns the portal's own bases.
	 * @return The portal's own bases.
	 */
	public List<BaseDTO> getBases() {
		return bases;
	}
	
	/**
	 * Sets the portal's own bases.
	 * @param bases The portal's own bases.
	 */
	public void setBases(List<BaseDTO> bases) {
		this.bases = bases;
	}
}
