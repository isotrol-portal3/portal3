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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.data.ModelData;
import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.pms.api.portal.PortalInheritableFlag;

/**
 * ModelData for {@link PortalInheritableFlag}
 * @author Andres Rodriguez
 */
public final class PortalInheritableFlagModelData implements ModelData {
	/** Key property. */
	public static final String KEY = "key";
	/** Display name property. */
	public static final String DISPLAY_NAME = "displayName";
	/** Properties collection. */
	private static final Set<String> PROPERTIES = DTOModelData.propertySet(KEY, DISPLAY_NAME);

	/** Display names map. */
	private final Map<PortalInheritableFlag, String> map;
	/** Current value. */
	private PortalInheritableFlag value;

	/**
	 * Constructor
	 * @param map Display names map.
	 * @param value Initial value.
	 */
	public PortalInheritableFlagModelData(Map<PortalInheritableFlag, String> map, PortalInheritableFlag value) {
		this.map = map;
		this.value = value != null ? value : PortalInheritableFlag.INHERIT;
	}

	/** Returns the display name for the provided value. */
	private String getDisplayName(PortalInheritableFlag value) {
		if (value == null) {
			value = PortalInheritableFlag.INHERIT;
		}
		return map.get(value);
	}

	/** Returns the display name for the current value. */
	private String getDisplayName() {
		return getDisplayName(value);
	}
	
	/** Returns the flag value. */
	public PortalInheritableFlag getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#get(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <X> X get(String property) {
		if (KEY.equals(property)) {
			return (X) value;
		} else if (DISPLAY_NAME.equals(property)) {
			return (X) getDisplayName();
		}
		throw new IllegalArgumentException("Invalid property");
	}

	/*
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getProperties()
	 */
	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KEY, value);
		map.put(DISPLAY_NAME, getDisplayName());
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

	/*
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#remove(java.lang.String)
	 */
	@Override
	public <X> X remove(String property) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#set(java.lang.String, java.lang.Object)
	 */
	@Override
	public <X> X set(String property, X value) {
		throw new UnsupportedOperationException();
	}
}
