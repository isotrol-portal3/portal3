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
import com.isotrol.impe3.pms.api.page.WithCategoryPageChildren;

/**
 * @author Manuel Ruiz
 *
 */
public class WithCategoryPageChildrenModelData<D extends WithCategoryPageChildren> extends DTOModelData<D> {

	/** descriptor for own pages category */
	public static final String PROPERTY_OWNPAGES = "own-pages";
	/** descriptor for own pages category */
	public static final String PROPERTY_INHPAGES = "inh-pages";
	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(AbstractMappingModelData.PROPERTIES, PROPERTY_OWNPAGES, PROPERTY_INHPAGES);
	
	/**
	 * @param dto
	 */
	public WithCategoryPageChildrenModelData(D dto) {
		super(dto);
	}

	@Override
	protected Object doGet(String property) {
		if (PROPERTY_OWNPAGES.equals(property)) {
			return getDTO().hasOwnPages();
		} else if (PROPERTY_INHPAGES.equals(property)) {
			return getDTO().hasInheritedPages();
		}
		
		throw new IllegalArgumentException("Not readable property: " + property);
	}

	@Override
	protected Object doSet(String property, Object value) {
		// nothing to write here:
		throw new IllegalArgumentException("Not writable property: " + property);
	}

	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}
}
