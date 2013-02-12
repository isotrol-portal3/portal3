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


import java.util.Map;

import com.isotrol.impe3.pms.api.AbstractWithId;
import com.isotrol.impe3.pms.api.NameDTO;


/**
 * Portal names and locales DTO.
 * @author Andres Rodriguez
 */
public class PortalNameDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = 8441555303614284517L;
	/** Name. */
	private NameDTO name;
	/** Portal description. */
	private String description;
	/** Default locale. */
	private String defaultLocale;
	/** Available locales. */
	private Map<String, String> locales;

	/** Default constructor. */
	public PortalNameDTO() {
	}

	/**
	 * Returns the name.
	 * @return The name.
	 */
	public NameDTO getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public void setName(NameDTO name) {
		this.name = name;
	}

	/**
	 * Returns the portal description.
	 * @return The portal description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the portal description.
	 * @param description The portal description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the default locale.
	 * @return The default locale.
	 */
	public String getDefaultLocale() {
		return defaultLocale;
	}

	/**
	 * Sets the default locale.
	 * @param defaultLocale The default locale.
	 */
	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	/**
	 * Returns the available locales.
	 * @return The available locales.
	 */
	public Map<String, String> getLocales() {
		return locales;
	}

	/**
	 * Sets the available locales.
	 * @param locales The available locales.
	 */
	public void setLocales(Map<String, String> locales) {
		this.locales = locales;
	}
}
