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

import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AInheritedModelData;

/**
 * ModelData for {@link Inherited}{@link ModuleInstanceSelDTO &lt;ModuleInstanceSelDTO&gt;}
 * @author Andrei Cojocaru
 *
 */
public class InheritedModuleInstanceSelModelData extends AInheritedModelData<ModuleInstanceSelDTO> {

	/**
	 * <br/>
	 * @param dto
	 */
	public InheritedModuleInstanceSelModelData(Inherited<ModuleInstanceSelDTO> dto) {
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
	 * Public properties for this class.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(AInheritedModelData.PROPERTIES,
			PROPERTY_DESCRIPTION,PROPERTY_ID,PROPERTY_NAME,PROPERTY_STATE,PROPERTY_KEY);
	
	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		Object res = null;
		ModuleInstanceSelDTO dto = getDTO().getValue();
		if (property.equals(PROPERTY_DESCRIPTION)) {
			res = dto.getDescription();
		} else if (property.equals(PROPERTY_ID)) {
			res = dto.getId();
		} else if (property.equals(PROPERTY_NAME)) {
			res = dto.getName();
		} else if (property.equals(PROPERTY_STATE)) {
			res = dto.getState();
		} else if (property.equals(PROPERTY_KEY)) {
			res = dto.getKey();
		} else {
			res = super.doGet(property);
		}
			
		return res;
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		// no need to write this class properties: module instances properties are  set through templates.
		return super.doSet(property, value);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
