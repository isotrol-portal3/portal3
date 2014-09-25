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

package com.isotrol.impe3.web20.api;


/**
 * DTO for a managed tag.
 * @author Andres Rodriguez
 */
public class TagDTO extends AbstractTagDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 6785933162041594559L;

	/** Whether the tag is valid. */
	private boolean valid;

	/**
	 * Constructor.
	 * @param name Tag name.
	 * @param valid Whether the tag is valid.
	 */
	public TagDTO(String name, boolean valid) {
		super(name);
		this.valid = valid;
	}

	/** Constructor. */
	public TagDTO() {
	}

	/**
	 * Returns whether the tag is valid.
	 * @return True if the tag is valid.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Sets whether the tag is valid.
	 * @param valid True if the tag is valid.
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
