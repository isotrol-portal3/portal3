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


/**
 * Model for localized names
 * @author Manuel Ruiz
 * 
 */
public class LocalizedNameModel extends LocaleNameModel {

	/**
	 * Generated serial version UID.<br/>
	 */
	private static final long serialVersionUID = 4105745194061927391L;

	/** property: "Path" */
	public static final String PROPERTY_PATH = "path";

	/**
	 * No-params constructor.<br/>
	 */
	public LocalizedNameModel() {
	}

	/**
	 * Constructor
	 * @param locale
	 * @param name
	 * @param path
	 */
	public LocalizedNameModel(String locale, String name, String path) {
		super(locale, name);
		setPath(path);
	}

	/**
	 * Returns the path.
	 * @return the path.
	 */
	public String getPath() {
		return get(PROPERTY_PATH);
	}

	/**
	 * Sets the path.
	 * @param path
	 */
	public final void setPath(String path) {
		set(PROPERTY_PATH, path);
	}
}
