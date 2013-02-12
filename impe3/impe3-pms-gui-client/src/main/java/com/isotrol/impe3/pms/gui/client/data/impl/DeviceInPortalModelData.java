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


import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.api.DeviceNameUse;
import com.isotrol.impe3.pms.api.portal.DeviceInPortalDTO;


/**
 * Model for portal devices
 * @author Manuel Ruiz
 * 
 */
public class DeviceInPortalModelData extends AbstractDeviceInPortalModelData<DeviceInPortalDTO> {

	/**
	 * <b>Active</b> property descriptor
	 */
	public static final String PROPERTY_ACTIVE = "active";

	/**
	 * <b>Device use name</b> property descriptor
	 */
	public static final String PROPERTY_USE_NAME = "usename";

	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(AbstractDeviceInPortalModelData.PROPERTIES,
		PROPERTY_ACTIVE, PROPERTY_USE_NAME);

	/**
	 * Default constructor
	 */
	public DeviceInPortalModelData(DeviceInPortalDTO dto) {
		super(dto);
	}

	@Override
	protected Object doGet(String property) {
		Object res = null;

		if (PROPERTY_ACTIVE.equals(property)) {
			res = getDTO().isActive();
		} else if (PROPERTY_USE_NAME.equals(property)) {
			res = getDTO().getUse();
		} else {
			res = super.doGet(property);
		}

		return res;
	}

	@Override
	protected Object doSet(String property, Object value) {
		DeviceInPortalDTO dto = getDTO();
		if (PROPERTY_PATH.equals(property)) {
			String old = dto.getName();
			dto.setName((String) value);
			return old;
		} else if (PROPERTY_ACTIVE.equals(property)) {
			Boolean old = dto.isActive();
			dto.setActive((Boolean) value);
			return old;
		} else if (PROPERTY_DEFAULT.equals(property)) {
			Boolean old = dto.isDefaultDevice();
			dto.setDefaultDevice((Boolean) value);
			return old;
		} else if (PROPERTY_USE_NAME.equals(property)) {
			DeviceNameUse old = dto.getUse();
			dto.setUse((DeviceNameUse) value);
			return old;
		}
		return super.doGet(property);
	}

	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}
}
