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


import java.io.Serializable;


/**
 * DTO representing a portal base.
 * @author Andres Rodriguez
 */
public final class BaseDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -7462850984435669021L;
	/** Base key. */
	private String key;
	/** Base URI. */
	private String uri;

	/** Default constructor. */
	public BaseDTO() {
	}

	/**
	 * Constructor.
	 * @param key The base key.
	 * @param uri The base URI.
	 */
	public BaseDTO(String key, String uri) {
		this.key = key;
		this.uri = uri;
	}

	/**
	 * Returns the base key.
	 * @return The base key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the base key.
	 * @param key The base key.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns the base URI.
	 * @return The base URI.
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Sets the base URI.
	 * @param uri The base URI.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

}
