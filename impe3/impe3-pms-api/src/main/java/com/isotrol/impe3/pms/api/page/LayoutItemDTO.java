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


import com.isotrol.impe3.pms.api.AbstractDescribedWithId;


/**
 * DTO for a page layout item.
 * @author Andres Rodriguez
 */
public final class LayoutItemDTO extends AbstractDescribedWithId {
	/** Serial UID. */
	private static final long serialVersionUID = -8129933226040765931L;
	/** Component markup. */
	private String markup;
	/** Whether the item is the space component. */
	private boolean space;

	/** Default constructor. */
	public LayoutItemDTO() {
	}

	/**
	 * Returns the component markup
	 * @param size Requested frame size.
	 * @return The component markup most suitable for the requested size.
	 */
	public String getMarkup(int size) {
		return markup;
	}

	/**
	 * Sets a constant component markup.
	 * @param markup Component markup.
	 */
	public void setMarkup(String markup) {
		this.markup = markup;
	}

	/**
	 * Returns whether the item is the space component.
	 * @return True if the item is the space component.
	 */
	public boolean isSpace() {
		return space;
	}

	/**
	 * Sets whether the item is the space component.
	 * @param space True if the item is the space component.
	 */
	public void setSpace(boolean space) {
		this.space = space;
	}
}
