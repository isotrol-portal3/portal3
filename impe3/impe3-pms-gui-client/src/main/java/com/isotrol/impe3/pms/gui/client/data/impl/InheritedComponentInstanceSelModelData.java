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
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;


/**
 * ModelData for InheritedComponentInstanceSelDTO
 * @author Manuel Ruiz
 * 
 */
public class InheritedComponentInstanceSelModelData extends DTOModelData<InheritedComponentInstanceSelDTO> {

	/**
	 * <br/>
	 * @param dto
	 */
	public InheritedComponentInstanceSelModelData(InheritedComponentInstanceSelDTO dto) {
		super(dto);
	}

	/**
	 * <b>State</b> property descriptor<br/>
	 */
	public static final String PROPERTY_STATE = Constants.PROPERTY_STATE;

	/**
	 * <b>ID</b> property descriptor<br/>
	 */
	public static final String PROPERTY_ID = Constants.PROPERTY_ID;

	/**
	 * <b>Name</b> property descriptor<br/>
	 */
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;

	/**
	 * <b>Description</b> property descriptor<br/>
	 */
	public static final String PROPERTY_DESCRIPTION = Constants.PROPERTY_DESCRIPTION;

	/**
	 * <b>Key</b> property descriptor<br/>
	 */
	public static final String PROPERTY_KEY = Constants.PROPERTY_KEY;
	
	/**
	 * <b>Key</b> property dependencies<br/>
	 */
	public static final String PROPERTY_DEPENDENCIES = "dependencies";
	
	/**
	 * <b>Key</b> property configuration<br/>
	 */
	public static final String PROPERTY_CONFIGURATION = "configuration";
	
	/** Descriptor for modules parent name property */
	public static final String PROPERTY_MODULE = "module";

	/**
	 * Public properties for this class.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_DESCRIPTION, PROPERTY_ID, PROPERTY_NAME,
		PROPERTY_STATE, PROPERTY_KEY, PROPERTY_DEPENDENCIES, PROPERTY_CONFIGURATION, PROPERTY_MODULE);

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		Object res = null;
		InheritedComponentInstanceSelDTO dto = getDTO();
		ModuleInstanceSelDTO component = dto.getComponent();
		if (property.equals(PROPERTY_DESCRIPTION)) {
			res = component.getDescription();
		} else if (property.equals(PROPERTY_ID)) {
			res = component.getId();
		} else if (property.equals(PROPERTY_NAME)) {
			res = component.getName();
		} else if (property.equals(PROPERTY_STATE)) {
			res = component.getState();
		} else if (property.equals(PROPERTY_KEY)) {
			res = component.getKey();
		} else if (property.equals(PROPERTY_DEPENDENCIES)) {
			res = dto.getDependencies();
		} else if (property.equals(PROPERTY_CONFIGURATION)) {
			res = dto.getConfiguration();
		} else if (property.equals(PROPERTY_MODULE)) {
			res = component.getModule().getName();
		} else {
			throw new IllegalArgumentException("Property not readable: " + property);
		}

		return res;
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		throw new IllegalArgumentException("Property not writable: " + property);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
