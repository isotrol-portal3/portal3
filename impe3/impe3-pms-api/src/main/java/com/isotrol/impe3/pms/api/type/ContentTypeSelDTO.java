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

package com.isotrol.impe3.pms.api.type;


import com.isotrol.impe3.pms.api.AbstractRoutable;


/**
 * DTO for content type selectors.
 * @author Andres Rodriguez
 */
public final class ContentTypeSelDTO extends AbstractRoutable {
	/** Serial UID. */
	private static final long serialVersionUID = -7774922107672233235L;
	/** Content type name. */
	private String name;
	/** Content type description. */
	private String description;
	/** Whether the content type is navigable. */
	private boolean navigable = true;

	/** Default constructor. */
	public ContentTypeSelDTO() {
	}

	/**
	 * Returns the content type name.
	 * @return The content type name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the content type name.
	 * @param name The content type name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the content type description.
	 * @return The content type description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the content type description.
	 * @param description The content type description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns whether the content type is navigable.
	 * @return True if the content type is navigable.
	 */
	public boolean isNavigable() {
		return navigable;
	}

	/**
	 * Sets whether the content type is navigable.
	 * @param navigable True if the content type is navigable.
	 */
	public void setNavigable(boolean navigable) {
		this.navigable = navigable;
	}

}
