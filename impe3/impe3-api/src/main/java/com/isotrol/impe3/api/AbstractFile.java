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

package com.isotrol.impe3.api;


import java.util.UUID;


/**
 * Abstract class for object that represents a server stored file. Files are used to store binary
 * objects used during configuration.
 * @author Andres Rodriguez.
 */
public abstract class AbstractFile extends AbstractIdentifiable {
	/** File name. */
	private final String name;
	
	/**
	 * Default constructor.
	 * @param id ID if the file.
	 * @param name File name.
	 */
	AbstractFile(final UUID id, final String name) {
		super(id);
		this.name = name;
	}

	/**
	 * Returns the file name.
	 * @return The file name
	 */
	public final String getName() {
		return name;
	}
}
