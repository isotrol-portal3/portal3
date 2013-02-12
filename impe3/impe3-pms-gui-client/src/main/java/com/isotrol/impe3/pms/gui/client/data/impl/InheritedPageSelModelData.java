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
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AInheritedModelData;


/**
 * Model for inherited pages
 * @author Manuel Ruiz
 * 
 */
public class InheritedPageSelModelData extends AInheritedModelData<PageSelDTO> {

	/**
	 * <b>Id</b> property descriptor<br/>
	 */
	public static final String PROPERTY_ID = Constants.PROPERTY_ID;

	/**
	 * <b>Name</b> property descriptor<br/>
	 */
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;

	/**
	 * <b>Description</b> property descriptor<br/>
	 */
	public static final String PROPERTY_DESCRIPTION = Constants.PROPERTY_DESCRIPTION;

	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(AInheritedModelData.PROPERTIES, PROPERTY_ID,
		PROPERTY_NAME, PROPERTY_DESCRIPTION);

	/** Indicates if it'is a page or is a folder. If false, it's a folder */
	private boolean isPage = true;

	/**
	 * Default constructor
	 */
	public InheritedPageSelModelData(Inherited<PageSelDTO> dto) {
		super(dto);
	}

	@Override
	protected Object doGet(String property) {
		PageSelDTO dto = getDTO().getValue();
		if (PROPERTY_NAME.equals(property)) {
			return dto.getName();
		} else if (PROPERTY_DESCRIPTION.equals(property)) {
			return dto.getDescription();
		} else if (PROPERTY_ID.equals(property)) {
			return dto.getId();
		}
		return super.doGet(property);
	}

	@Override
	protected Object doSet(String property, Object value) {
		return super.doSet(property, value);
	}

	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

	/**
	 * @return the isPage
	 */
	public boolean isPage() {
		return isPage;
	}

	/**
	 * @param page the isPage to set
	 */
	public void setPage(boolean page) {
		this.isPage = page;
	}
}
