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

import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.portal.AbstractDeviceInPortalDTO;


/**
 * Abstract model for device selection in a portal.
 * @author Manuel Ruiz
 * 
 */
public class AbstractDeviceInPortalModelData<D extends AbstractDeviceInPortalDTO> extends DTOModelData<D> {

	/**
	 * <b>Name</b> property descriptor<br/>
	 */
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;

	/**
	 * <b>Default device</b> property descriptor<br/>
	 */
	public static final String PROPERTY_DEFAULT = "default";

	/**
	 * <b>Device id</b> property descriptor<br/>
	 */
	public static final String PROPERTY_DEVICE_ID = Constants.PROPERTY_ID;

	/**
	 * <b>Path</b> property descriptor<br/>
	 */
	public static final String PROPERTY_PATH = "path";

	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_NAME, PROPERTY_DEFAULT, PROPERTY_DEVICE_ID,
		PROPERTY_PATH);

	/**
	 * Default constructor
	 */
	public AbstractDeviceInPortalModelData(D dto) {
		super(dto);
	}

	@Override
	protected Object doGet(String property) {
		Object res = null;

		if (PROPERTY_NAME.equals(property)) {
			res = getDTO().getDevice().getName();
		} else if (PROPERTY_DEFAULT.equals(property)) {
			res = getDTO().isDefaultDevice();
		} else if (PROPERTY_DEVICE_ID.equals(property)) {
			res = getDTO().getDeviceId();
		} else if (PROPERTY_PATH.equals(property)) {
			res = getDTO().getName();
		} else {
			throw new IllegalArgumentException("Not readable property: " + property);
		}

		return res;
	}

	@Override
	protected Object doSet(String property, Object value) {
		throw new IllegalArgumentException("Not writable property: " + property);
	}

	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}
}
