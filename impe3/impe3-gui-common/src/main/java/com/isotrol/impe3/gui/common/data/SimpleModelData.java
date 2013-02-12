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

package com.isotrol.impe3.gui.common.data;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Many widgets need simple models that should store its identifier and display name.
 * This ModelData fits those needs publishing the properties {@link #PROPERTY_DISPLAY_NAME}
 * and {@link #PROPERTY_ID}.
 * 
 * @author Andrei Cojocaru
 *
 */
public class SimpleModelData extends BaseModelData {

	/**
	 * The serialization ID.<br/>
	 */
	private static final long serialVersionUID = 3346514301635537386L;

	/**
	 * <b>Display Name</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_DISPLAY_NAME = "dn";
	
	/**
	 * <b>ID</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_ID = "id";
	
	/**
	 * Default constructor. Does nothing.
	 */
	public SimpleModelData() {}
	
	/**
	 * Constructor provided with display name and ID values. 
	 * @param displayName the model display name.
	 * @param id the model ID.
	 */
	public SimpleModelData(String displayName, Object id) {
		set(PROPERTY_DISPLAY_NAME, displayName);
		set(PROPERTY_ID, id);
	}
	
	/**
	 * Returns the display name.<br/>
	 * Synonym for <code>get(PROPERTY_DISPLAY_NAME)</code>
	 * @return the display name.
	 */
	public String getDisplayName() {
		return get(PROPERTY_DISPLAY_NAME);
	}
	
	/**
	 * Returns the model ID.<br/>
	 * Synonym for <code>get(PROPERTY_ID)</code>
	 * @param <X> the output class type
	 * @return the object stored under the key {@link #PROPERTY_ID}.
	 */
	@SuppressWarnings("unchecked")
	public <X> X getId() {
		return (X) get(PROPERTY_ID);
	}
}
