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

package com.isotrol.impe3.pms.api.portal;


import java.util.List;
import java.util.Set;

import com.isotrol.impe3.pms.api.AbstractWithId;


/**
 * Portal IA definition.
 * @author Andres Rodriguez
 */
public class PortalIADTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = -8663362870965272057L;
	/** Category mode. */
	private PortalCategoryMode mode;
	/** Categories. */
	private List<String> categories;
	/** Content types. */
	private Set<String> contentTypes;

	/** Default constructor. */
	public PortalIADTO() {
	}

	/**
	 * Returns the category mode.
	 * @return The category mode.
	 */
	public PortalCategoryMode getMode() {
		return mode;
	}

	/**
	 * Sets the category mode.
	 * @param name The category mode.
	 */
	public void setMode(PortalCategoryMode mode) {
		this.mode = mode;
	}

	/**
	 * Returns the first level of categories ids.
	 * @return The first level of categories ids.
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * Sets the first level of categories ids.
	 * @param categories The first level of categories ids.
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	/**
	 * Returns the content types ids.
	 * @return The content types ids.
	 */
	public Set<String> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Sets the content types ids.
	 * @param contentTypes The content types ids.
	 */
	public void setContentTypes(Set<String> contentTypes) {
		this.contentTypes = contentTypes;
	}
}
