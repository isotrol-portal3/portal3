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

package com.isotrol.impe3.pms.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.common.base.Objects;


/**
 * Value that represents a display name and path segment.
 * @author Andres Rodriguez
 */
@Embeddable
public class NameValue {
	/** Name. */
	@Column(name = "NAME", nullable = false, length = Lengths.NAME)
	private String name;
	/** Path segment. */
	@Column(name = "PATH", nullable = true, length = Lengths.NAME)
	private String path;

	/** Default constructor. */
	public NameValue() {
	}

	/**
	 * Constructor.
	 * @param name Name.
	 * @param path Path segment.
	 */
	public NameValue(String name, String path) {
		this.name = name;
		this.path = path;
	}

	/**
	 * Copy constructor.
	 * @param name Name.
	 */
	public NameValue(NameValue name) {
		this(name.name, name.path);
	}

	/**
	 * Returns the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the path segment.
	 * @return The path segment.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path segment.
	 * @param path The path segment.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return Objects.hashCode(name, path);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof NameValue) {
			final NameValue m = (NameValue) obj;
			return Objects.equal(name, m.name) && Objects.equal(path, m.path);
		}
		return false;
	}

}
