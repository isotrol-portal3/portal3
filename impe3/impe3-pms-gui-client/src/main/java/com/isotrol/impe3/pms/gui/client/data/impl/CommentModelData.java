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
import com.isotrol.impe3.web20.api.CommentDTO;


/**
 * @author Manuel Ruiz
 * 
 */
public class CommentModelData extends DTOModelData<CommentDTO> {

	/**
	 * <b>Date</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_DATE = "date";

	/**
	 * <b>Author</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_AUTHOR = "author";

	/**
	 * <b>Comment string</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_COMMENT = "comment";

	/**
	 * <b>Resource id string</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_RESOURCE_ID = "resource-id";

	/**
	 * <b>Resource string</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_RESOURCE = "resource";
	
	/**
	 * <b>Valid string</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_VALID = "valid";
	
	/**
	 * <b>Last moderated string</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_LAST_MODERATION = "moderated";

	/**
	 * Published properties.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_DATE, PROPERTY_AUTHOR, PROPERTY_COMMENT,
		PROPERTY_RESOURCE_ID, PROPERTY_RESOURCE, PROPERTY_VALID, PROPERTY_LAST_MODERATION);

	/**
	 * @param dto
	 */
	public CommentModelData(CommentDTO dto) {
		super(dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	/**
	 * <br/>
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.gui.common.data.DTOModelData#doGet(java.lang.String)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected Object doGet(String property) {
		Object value = null;
		CommentDTO dto = getDTO();

		if (property.equals(PROPERTY_AUTHOR)) {
			value = dto.getTitle();
		} else if (property.equals(PROPERTY_COMMENT)) {
			value = dto.getDescription();
		} else if (property.equals(PROPERTY_DATE)) {
			value = dto.getDate();
		} else if (property.equals(PROPERTY_RESOURCE_ID)) {
			value = dto.getResourceKey();
		} else if (property.equals(PROPERTY_RESOURCE)) {
			value = dto.getResourceKey();
		} else if (property.equals(PROPERTY_VALID)) {
			value = dto.isValid();
		} else if (property.equals(PROPERTY_LAST_MODERATION)) {
			value = dto.getLastModeration();
		} else {
			throw new UnsupportedOperationException("Property not readable: " + property);
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.gui.common.data.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected Object doSet(String property, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

}
