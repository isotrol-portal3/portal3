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

import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId;


/**
 * MoodelData for Module Instances
 * @author Manuel Ruiz
 * 
 */
public class ModuleInstanceSelModelData extends AbstractDTOModelDataWithStateAndId<ModuleInstanceSelDTO> {

	/** Descriptor for module's name property */
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;
	/** Descriptor for module's description property */
	public static final String PROPERTY_DESCRIPTION = Constants.PROPERTY_DESCRIPTION;
	/** Descriptor for module's key property */
	public static final String PROPERTY_KEY = Constants.PROPERTY_KEY;
	/** Descriptor for modules parent name property */
	public static final String PROPERTY_MODULE = "module";
	/** Descriptor for correcteness property */
	public static final String PROPERTY_CORRECTNESS = Constants.PROPERTY_CORRECTNESS;

	/**
	 * Public properties for this class.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(AbstractDTOModelDataWithStateAndId.PROPERTIES,
		PROPERTY_DESCRIPTION, PROPERTY_NAME, PROPERTY_KEY, PROPERTY_MODULE, PROPERTY_CORRECTNESS);

	/**
	 * <br/>
	 * @param dto
	 */
	public ModuleInstanceSelModelData(ModuleInstanceSelDTO dto) {
		super(dto);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if (property.equals(PROPERTY_DESCRIPTION)) {
			return getDTO().getDescription();
		} else if (property.equals(PROPERTY_NAME)) {
			return getDTO().getName();
		} else if (property.equals(PROPERTY_KEY)) {
			return getDTO().getKey();
		} else if (property.equals(PROPERTY_MODULE)) {
			return getDTO().getModule().getName();
		} else if (property.equals(PROPERTY_CORRECTNESS)) {
			return getDTO().getCorrectness();
		}
		return super.doGet(property);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId#doSet(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		throw new IllegalArgumentException(property);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithStateAndId#getPropertyNames()
	 */
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
