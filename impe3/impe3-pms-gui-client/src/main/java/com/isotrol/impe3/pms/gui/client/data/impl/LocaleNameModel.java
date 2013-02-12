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


import com.extjs.gxt.ui.client.data.BaseModel;


/**
 * Model for portal name locales
 * @author Manuel Ruiz
 * 
 */
public class LocaleNameModel extends BaseModel {
	
	/**
	 * Generated serial version UID.<br/>
	 */
	private static final long serialVersionUID = -3197249721891393139L;
	
	/** property: "Locale" */
	public static final String PROPERTY_LOCALE = "locale";
	/** property: "Name" */
	public static final String PROPERTY_NAME = "name";

	/**
	 * No-params constructor.<br/>
	 */
	public LocaleNameModel() {
	}

	/**
	 * Constructor
	 * @param locale
	 * @param name
	 * @param path
	 */
	public LocaleNameModel(String locale, String name) {
		setLocale(locale);
		setName(name);
	}

	/**
	 * Returns the locale.
	 * @return the locale.
	 */
	public String getLocale() {
		return get(PROPERTY_LOCALE);
	}

	/**
	 * Returns the name.
	 * @return the name.
	 */
	public String getName() {
		return get(PROPERTY_NAME);
	}

	/**
	 * Sets the locale.
	 * @param locale
	 */
	public final void setLocale(String locale) {
		set(PROPERTY_LOCALE, locale);
	}

	/**
	 * Sets the name.
	 * @param name
	 */
	public final void setName(String name) {
		set(PROPERTY_NAME, name);
	}
}
