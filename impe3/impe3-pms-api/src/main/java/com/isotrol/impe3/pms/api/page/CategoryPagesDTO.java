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

package com.isotrol.impe3.pms.api.page;


import com.isotrol.impe3.pms.api.Inherited;


/**
 * DTO representing a category node of the portal pages tree for a device.
 * @author Andres Rodriguez
 */
public final class CategoryPagesDTO extends WithCategoryPageChildren {
	/** Serial UID. */
	private static final long serialVersionUID = 1345430712241144568L;
	/** Default page. */
	private Inherited<PageLoc> defaultPage;

	/** Default constructor. */
	public CategoryPagesDTO() {
	}

	/**
	 * Returns the default page.
	 * @return The default page.
	 */
	public Inherited<PageLoc> getDefaultPage() {
		return defaultPage;
	}

	/**
	 * Sets the default page.
	 * @param defaultPage The default page.
	 */
	public void setDefaultPage(Inherited<PageLoc> defaultPage) {
		this.defaultPage = defaultPage;
	}
}
