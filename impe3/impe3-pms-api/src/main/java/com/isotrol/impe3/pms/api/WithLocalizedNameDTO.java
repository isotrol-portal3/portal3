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

package com.isotrol.impe3.pms.api;


import java.util.Map;


/**
 * Interace for a DTO with default name and localized name.
 * @author Andres Rodriguez
 */
public interface WithLocalizedNameDTO {
	/**
	 * Returns the default name.
	 * @return The default name.
	 */
	NameDTO getDefaultName();

	/**
	 * Sets the default name.
	 * @param name The default name.
	 */
	void setDefaultName(NameDTO name);

	/**
	 * Returns the localized names.
	 * @return The localized names.
	 */
	Map<String, NameDTO> getLocalizedNames();

	/**
	 * Sets the localized names.
	 * @param localizedNames The localized names.
	 */
	void setLocalizedNames(Map<String, NameDTO> localizedNames);
}
