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

package com.isotrol.impe3.pms.api.mreg;


/**
 * DTO for a connectior exported by a module.
 * @author Andres Rodriguez
 */
public final class ProvidedComponentDTO extends ModuleRelationDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 8845616318491900484L;
	/** Version information. */
	private String version;
	/** Copyright information. */
	private String copyright;

	/** Default constructor. */
	public ProvidedComponentDTO() {
	}

	/**
	 * Returns the version.
	 * @return The version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * @param version The version.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Returns the copyright.
	 * @return The copyright.
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * Sets the copyright.
	 * @param copyright The copyright.
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

}
