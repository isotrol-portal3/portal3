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
import com.isotrol.impe3.pms.api.minst.ProviderDTO;


/**
 * ModelData for ProviderDTO
 * @author Manuel Ruiz
 * 
 */
public class ProviderModelData extends DTOModelData<ProviderDTO> {

	/** Descriptor for provider's description property */
	public static final String PROPERTY_DESCRIPTION = Constants.PROPERTY_DESCRIPTION;
	/** Descriptor for provider's bean property */
	public static final String PROPERTY_BEAN = Constants.PROPERTY_BEAN;
	/** Descriptor for provider's name property */
	public static final String PROPERTY_TO_DISPLAY = "display";
	/** Descriptor for the single property */
	public static final String PROPERTY_UNIQUE = "UNIQUE";

	/**
	 * <br/>
	 * @param dto
	 */
	public ProviderModelData(ProviderDTO dto) {
		super(dto);
	}

	/**
	 * Public properties for this class instances.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_DESCRIPTION, PROPERTY_BEAN, PROPERTY_TO_DISPLAY,
		PROPERTY_UNIQUE);

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {

		if (PROPERTY_DESCRIPTION.equals(property)) {
			return getDTO().getDescription();
		} else if (PROPERTY_BEAN.equals(property)) {
			return getDTO().getBean();
		} else if (PROPERTY_TO_DISPLAY.equals(property)) {
			return getDTO().getCurrent().getName() + " :: " + getDTO().getBean();
		} else if (PROPERTY_UNIQUE.equals(property)) {
			return getDTO().getCurrent().getId() + getDTO().getBean();
		}

		throw new IllegalArgumentException("Not readable property: " + property);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		// nothing to write here:
		throw new IllegalArgumentException("Not writable property: " + property);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
