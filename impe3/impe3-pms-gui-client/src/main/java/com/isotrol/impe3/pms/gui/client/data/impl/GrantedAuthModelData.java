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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.data.impl;

import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.pms.api.user.AbstractAuthDTO;
import com.isotrol.impe3.pms.api.user.Granted;

/**
 * Base class for ModelData that represent specializations of {@link AbstractAuthDTO}.
 * 
 * @author Andrei Cojocaru
 *
 * @param <D> granted artifact type.
 */
public class GrantedAuthModelData<D extends AbstractAuthDTO> 
	extends AbstractGrantedModelData<D> {

	/**
	 * <b>Display name</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_DISPLAY_NAME = "display_name";
	
	/**
	 * Public properties.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(
			AbstractGrantedModelData.PROPERTIES, PROPERTY_DISPLAY_NAME);
	
	/**
	 * @param dto
	 */
	public GrantedAuthModelData(Granted<D> dto) {
		super(dto);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected Object doGet(String property) {
		Object res = null;
		if (property.equals(PROPERTY_DISPLAY_NAME)) {
			res = getDTO().get().getDisplayName();
		} else {
			res = super.doGet(property);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	/**
	 * <br/>
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
