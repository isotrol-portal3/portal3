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

package com.isotrol.impe3.pms.gui.client.data.model;

import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.Described;

/**
 * Base ModelData for Described instances.
 * 
 * @author Andrei Cojocaru
 *
 * @param <D>
 */
public abstract class AbstractDescribedModelData<D extends Described> extends DTOModelData<D> {

	/**
	 * Constructor provided with bound DTO.<br/>
	 * @param dto
	 */
	public AbstractDescribedModelData(D dto) {
		super(dto);
	}

	/**
	 * <code>Name</code> property descriptor.<br/>
	 */
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;
	
	/**
	 * <code>Description</code> property descriptor.<br/>
	 */
	public static final String PROPERTY_DESCRIPTION = Constants.PROPERTY_DESCRIPTION;
	
	/**
	 * Public properties set.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_NAME, PROPERTY_DESCRIPTION);
	
	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		String res = null;
		
		Described dto = getDTO();
		
		if (property.equals(PROPERTY_NAME)) {
			res = dto.getName();
		} else if (property.equals(PROPERTY_DESCRIPTION)) {
			res = dto.getDescription();
		} else {
			throw new IllegalArgumentException("No such property: " + property);
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
		throw new IllegalArgumentException("Not writable property: " + property);
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
