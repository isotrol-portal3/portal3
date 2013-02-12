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

import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.WithId;
import com.isotrol.impe3.pms.api.WithState;

/**
 * @author Andrei Cojocaru
 * 
 * @param <D>
 */
public class AbstractDTOModelDataWithStateAndId<D extends WithState & WithId> extends AbstractDTOModelDataWithState<D> {

	/**
	 * <b>ID</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_ID = Constants.PROPERTY_ID;

	/**
	 * public properties for this object instances.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(AbstractDTOModelDataWithState.PROPERTIES, PROPERTY_ID);

	/**
	 * <br/>
	 * 
	 * @param dto
	 */
	public AbstractDTOModelDataWithStateAndId(D dto) {
		super(dto);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithState#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if (property.equals(PROPERTY_ID)) {
			return getDTO().getId();
		}
		return super.doGet(property);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithState#doSet(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		// if(property.equals(PROPERTY_ID)) {
		// WithId dto = getDTO();
		// String old = dto.getId();
		// dto.setId((String) value);
		// return old;
		// }
		return super.doSet(property, value);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithState#getPropertyNames()
	 */
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

	/**
	 * Two instances of AbstractModelDataWithStateAndId are equal if their DTOs
	 * have the same ID.<br/>
	 * Used to compute the dirty state in fields where ModelData instances can
	 * be lost (e.g. widgets that get destroyed and created instead of
	 * showing/hiding).<br/>
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractDTOModelDataWithStateAndId<?>)) {
			return false;
		}
		return getDTO().getId().equals(
				((AbstractDTOModelDataWithStateAndId<?>) obj).getDTO().getId());
	}

}
