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

import com.isotrol.impe3.pms.api.user.UserSelDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithId;

/**
 * ModelData for UserSelDTO objects.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 *
 */
public class UserSelModelData extends AbstractDTOModelDataWithId<UserSelDTO> {
	
	/**
	 * <b>Username</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_USERNAME = "user_name";
	
	/**
	 * <b>Display name</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_DISPLAY_NAME = "display_name";
	
	/**
	 * <b>Admin</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_ADMIN = "admin";
	
	/**
	 * <b>Active</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_ACTIVE = "active";
	
	/**
	 * <b>Locked</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_LOCKED = "lock";
	
	/**
	 * Public properties.<br/>
	 */
	private static final Set<String> PROPERTIES = propertySet(
			AbstractDTOModelDataWithId.PROPERTIES, PROPERTY_USERNAME, PROPERTY_DISPLAY_NAME, 
			PROPERTY_ADMIN, PROPERTY_ACTIVE, PROPERTY_LOCKED);
	
	/**
	 * @param dto
	 */
	public UserSelModelData(UserSelDTO dto) {
		super(dto);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDTOModelDataWithId#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		Object val = null;
		if (property.equals(PROPERTY_ADMIN)) {
			val = getDTO().isRoot();
		} else if (property.equals(PROPERTY_USERNAME)) {
			val = getDTO().getName();
		} else if (property.equals(PROPERTY_DISPLAY_NAME)){
			val = getDTO().getDisplayName();
		} else if (property.equals(PROPERTY_ACTIVE)) {
			val = getDTO().isActive();
		} else if (property.equals(PROPERTY_LOCKED)) {
			val = getDTO().isLocked();
		}else {
			val = super.doGet(property);
		}
		return val;
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDTOModelDataWithId
	 * #doSet(java.lang.String, java.lang.Object)
	 */
	/**
	 * Neither {@link #PROPERTY_ADMIN} nor {@link #PROPERTY_USERNAME} are writable.<br/>
	 */
	@Override
	protected Object doSet(String property, Object value) {
		return super.doSet(property, value);
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDTOModelDataWithId#getPropertyNames()
	 */
	/**
	 * <br/>
	 */
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}
	
}
