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
 * Model for user properties
 * @author manuel Ruiz
 * 
 */
public class UserPropertyModel extends BaseModel {

	/**
	 * Generated serial version UID.<br/>
	 */
	private static final long serialVersionUID = -7864231577025045303L;
	
	/** property: "Key" */
	public static final String PROPERTY_KEY = "key";
	/** property: "Value" */
	public static final String PROPERTY_VALUE = "value";

	/**
	 * No-params constructor.<br/>
	 */
	public UserPropertyModel() {
	}

	/**
	 * Constructor
	 * @param key
	 * @param value
	 */
	public UserPropertyModel(String key, String value) {
		setKey(key);
		setValue(value);
	}

	/**
	 * Returns the key.
	 * @return the key.
	 */
	public String getKey() {
		return get(PROPERTY_KEY);
	}

	/**
	 * Returns the value.
	 * @return the value.
	 */
	public String getValue() {
		return get(PROPERTY_VALUE);
	}

	/**
	 * Sets the key.
	 * @param key
	 */
	public final void setKey(String key) {
		set(PROPERTY_KEY, key);
	}

	/**
	 * Sets the value.
	 * @param value
	 */
	public final void setValue(String value) {
		set(PROPERTY_VALUE, value);
	}
}
