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

import com.isotrol.impe3.pms.api.page.PageDeviceDTO;


/**
 * Model for device selection in pages.
 * @author Manuel Ruiz
 * 
 */
public class PageDeviceModelData extends AbstractDeviceInPortalModelData<PageDeviceDTO> {

	/**
	 * <b>Active</b> property descriptor<br/>
	 */
	public static final String PROPERTY_DISPLAY_NAME = "display-name";

	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(AbstractDeviceInPortalModelData.PROPERTIES,
		PROPERTY_DISPLAY_NAME);

	/**
	 * Default constructor
	 */
	public PageDeviceModelData(PageDeviceDTO dto) {
		super(dto);
	}

	@Override
	protected Object doGet(String property) {
		Object res = null;

		if (PROPERTY_DISPLAY_NAME.equals(property)) {
			res = getDTO().getDisplayName();
		} else {
			res = super.doGet(property);
		}

		return res;
	}

	@Override
	protected Object doSet(String property, Object value) {
		return super.doSet(property, value);
	}

	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}
}
