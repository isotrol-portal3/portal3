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

import com.isotrol.impe3.pms.api.esvc.IndexerDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDescribedModelData;

public class IndexerModelData extends AbstractDescribedModelData<IndexerDTO>{

	public IndexerModelData(IndexerDTO dto) {
		super(dto);
		
	}
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_STATE = "state";
	public static final String PROPERTY_TYPE = "type";
	public static final String PROPERTY_NODE = "node";
	
	/**
	 * Public properties.<br/>
	 */
	public static final Set<String> PROPERTIES = 
		propertySet(AbstractDescribedModelData.PROPERTIES, PROPERTY_ID, PROPERTY_STATE, PROPERTY_TYPE, PROPERTY_NODE);


	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDescribedModelData#doGet(java.lang.String)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected Object doGet(String property) {
		if (property.equals(PROPERTY_ID)) {
			return getDTO().getId();
		}
		if (property.equals(PROPERTY_STATE)) {
			return getDTO().getState();
		}
		if (property.equals(PROPERTY_TYPE)) {
			return getDTO().getType();
		}
		if (property.equals(PROPERTY_NODE)) {
			return getDTO().getNode();
		}
		return super.doGet(property);
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.AbstractDescribedModelData#getPropertyNames()
	 */
	/**
	 * <br/>
	 */
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}
}
