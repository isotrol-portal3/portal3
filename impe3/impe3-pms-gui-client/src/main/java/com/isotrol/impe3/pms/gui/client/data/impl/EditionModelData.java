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
import com.isotrol.impe3.pms.api.edition.EditionDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithId;


/**
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class EditionModelData extends AbstractDTOModelDataWithId<EditionDTO> {

	/**
	 * <b>Creation date</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_CREATED = Constants.PROPERTY_CREATED;

	/**
	 * <b>Creation by</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_CREATED_BY = "created_by";

	/**
	 * <b>Publication date</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_PUBLISHED = "published";

	/**
	 * <b>Publication by</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_PUBLISHED_BY = "published_by";

	/**
	 * <b>Current</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_CURRENT = Constants.PROPERTY_CURRENT;

	/**
	 * Public propeties.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(AbstractDTOModelDataWithId.PROPERTIES, PROPERTY_CREATED,
		PROPERTY_CURRENT, PROPERTY_CREATED_BY, PROPERTY_PUBLISHED, PROPERTY_PUBLISHED_BY);

	/**
	 * ModelData constructor provided with bound DTO.<br/>
	 * @param dto
	 */
	public EditionModelData(EditionDTO dto) {
		super(dto);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		Object res = null;
		EditionDTO dto = getDTO();

		if (property.equals(PROPERTY_CREATED)) {
			res = dto.getCreated().getTimestamp();
		} else if (property.equals(PROPERTY_CREATED_BY)) {
			res = dto.getCreated().getUser().getDisplayName();
		} else if (property.equals(PROPERTY_PUBLISHED)) {
			res = dto.getLastPublished().getTimestamp();
		} else if (property.equals(PROPERTY_PUBLISHED_BY)) {
			res = dto.getLastPublished().getUser().getDisplayName();
		} else if (property.equals(PROPERTY_CURRENT)) {
			res = dto.isCurrent();
		} else {
			res = super.doGet(property);
		}

		return res;
	}

	/**
	 * Nothing to write at this hierarchy level.<br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		return super.doSet(property, value);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
