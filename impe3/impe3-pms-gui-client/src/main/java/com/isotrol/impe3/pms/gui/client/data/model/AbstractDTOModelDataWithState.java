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
package com.isotrol.impe3.pms.gui.client.data.model;


import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.WithState;


/**
 * @author Andrei Cojocaru
 * 
 * @param <D> DTO type variable
 */
public class AbstractDTOModelDataWithState<D extends WithState> extends DTOModelData<D> {

	/**
	 * <b>State</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_STATE = Constants.PROPERTY_STATE;

	/**
	 * Set of public properties for this data type.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_STATE);

	/**
	 * Constructor with the DTO passed.<br/>
	 * @param dto
	 */
	public AbstractDTOModelDataWithState(D dto) {
		super(dto);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.exp.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if (property.equals(PROPERTY_STATE)) {
			return getDTO().getState();
		}
		throw new IllegalArgumentException();
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.exp.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		throw new IllegalArgumentException();
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
