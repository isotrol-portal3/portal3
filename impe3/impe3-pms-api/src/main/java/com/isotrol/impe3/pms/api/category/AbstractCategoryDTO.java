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


import com.isotrol.impe3.pms.api.AbstractRoutable;


/**
 * Abstract class category DTOs.
 * @author Andres Rodriguez
 */
public abstract class AbstractCategoryDTO extends AbstractRoutable {
	/** Serial UID. */
	private static final long serialVersionUID = -7487066883749812192L;
	/** If the category is visible. */
	private boolean visible;

	/** Default constructor. */
	public AbstractCategoryDTO() {
	}

	/**
	 * Returns whether the category is visible.
	 * @return True if the category is visible.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets whether the category is visible.
	 * @param visible True if the category is visible.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
