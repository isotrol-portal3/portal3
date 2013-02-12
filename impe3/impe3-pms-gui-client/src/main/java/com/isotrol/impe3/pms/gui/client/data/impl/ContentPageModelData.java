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
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.ContentPageDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;

/**
 * ModelData for ContentPageDTO
 * 
 * @author Manuel Ruiz
 * 
 */
public class ContentPageModelData extends DTOModelData<ContentPageDTO> {

	/** Descriptor for content type name property */
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;

	/**
	 * <b>ID</b> property descriptor.<br/>
	 */
	public static final String PROPERTY_ID = Constants.PROPERTY_ID;
	
	/** Descriptor for withCategory property */
	public static final String PROPERTY_WITH_CATEGORY = "with-category";

	/**
	 * If <code>true</code>, the tree node associated to this ContentPageModelData instance 
	 * represents the default Content Type Page.<br/>
	 */
	private boolean defaultPage;

	/**
	 * <br/>
	 * 
	 * @param dto
	 */
	public ContentPageModelData(ContentPageDTO dto) {
		super(dto);
	}

	/**
	 * Public properties for this class instances.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_NAME, PROPERTY_ID, PROPERTY_WITH_CATEGORY);

	/**
	 * <br/>
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if (PROPERTY_NAME.equals(property)) {
			return getDTO().getContentType().getName();
		} else if (PROPERTY_ID.equals(property)) {
			Inherited<PageLoc> page = getDTO().getPage();
			if (page != null) {
				return page.getValue().getId();	
			}
			return null;
		} else if (PROPERTY_WITH_CATEGORY.equals(property)) {
			return getDTO().isWithCategory();
		}

		throw new IllegalArgumentException("Not readable property: " + property);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		// nothing to write here:
		throw new IllegalArgumentException("Not writable property: " + property);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * 
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

	/**
	 * @return the defaultPage
	 */
	public boolean isDefaultPage() {
		return defaultPage;
	}

	/**
	 * @param defaultPage
	 *            the defaultPage to set
	 */
	public void setDefaultPage(boolean defaultPage) {
		this.defaultPage = defaultPage;
	}
}
