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

import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.Described;
import com.isotrol.impe3.pms.api.WithId;

/**
 * ModelData for classes that implement {@link Described} and {@link WithId}.
 * 
 * @author Andrei Cojocaru
 * 
 * @param <D>
 */
public class AbstractDescribedWithIdModelData<D extends Described & WithId> extends AbstractDescribedModelData<D> {

	/**
	 * <code>ID</code> property descriptor.<br/>
	 */
	public static final String PROPERTY_ID = Constants.PROPERTY_ID;

	/**
	 * Public properties.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(AbstractDescribedModelData.PROPERTIES, PROPERTY_ID);

	/**
	 * Constructor provided with bound DTO.<br/>
	 * 
	 * @param dto
	 */
	public AbstractDescribedWithIdModelData(D dto) {
		super(dto);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDescribedModelData#doGet(java.lang.String)
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
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDescribedModelData#getPropertyNames()
	 */
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
