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

import com.isotrol.impe3.pms.api.AbstractWithStateAndId;


/**
 * DTO for category selection.
 * @author Andres Rodriguez
 */
public final class CategorySelDTO extends AbstractWithStateAndId {
	/** Serial UID. */
	private static final long serialVersionUID = -8475435071674532250L;
	/** Category name. */
	private String name;
	/** If the category is visible. */
	private boolean visible;
	/** If the category is routable. */
	private boolean routable;

	/** Default constructor. */
	public CategorySelDTO() {
	}

	/**
	 * Returns the category name.
	 * @return The category name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the category name.
	 * @param name The category name.
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * Returns whether the category is routable.
	 * @return True if the category is routable.
	 */
	public boolean isRoutable() {
		return routable;
	}

	/**
	 * Sets whether the category is routable.
	 * @param routable True if the category is routable.
	 */
	public void setRoutable(boolean routable) {
		this.routable = routable;
	}
}
