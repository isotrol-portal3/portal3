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

package com.isotrol.impe3.pms.api.category;


import java.util.Map;

import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.WithLocalizedNameDTO;


/**
 * DTO for category modification.
 * @author Andres Rodriguez
 */
public class CategoryDTO extends AbstractCategoryDTO implements WithLocalizedNameDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 8408826834213451551L;
	/** Content type default name. */
	private NameDTO defaultName;
	/** Content type localized names. */
	private Map<String, NameDTO> localizedNames;

	/** Default constructor. */
	public CategoryDTO() {
	}

	/**
	 * Returns the content type default name.
	 * @return The content type default name.
	 */
	public NameDTO getDefaultName() {
		return defaultName;
	}

	/**
	 * Sets the content type default name.
	 * @param name The content type default name.
	 */
	public void setDefaultName(NameDTO name) {
		this.defaultName = name;
	}

	/**
	 * Returns the localized names.
	 * @return The localized names.
	 */
	public Map<String, NameDTO> getLocalizedNames() {
		return localizedNames;
	}

	/**
	 * Sets the localized names.
	 * @param localizedNames The localized names.
	 */
	public void setLocalizedNames(Map<String, NameDTO> localizedNames) {
		this.localizedNames = localizedNames;
	}
}
