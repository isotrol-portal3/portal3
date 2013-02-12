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

package com.isotrol.impe3.pms.api;

import java.io.Serializable;


/**
 * DTO that represents a inherited value in a portal.
 * @param <V> Value type.
 * @author Andres Rodriguez
 */
public class Inherited<V> implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 5043278873118843622L;
	/** Whether the element is inherited. */
	private boolean inherited;
	/** Possibly inherited type. */
	private V value;

	/** Default constructor. */
	public Inherited() {
	}
	
	/**
	 * Constructor.
	 * @param inherited True if the element is inherited.
	 * @param value The possibly inherited type.
	 */
	public Inherited(boolean inherited, V value) {
		this.inherited = inherited;
		this.value = value;
	}

	/**
	 * Returns whether the element is inherited.
	 * @return True if the element is inherited.
	 */
	public boolean isInherited() {
		return inherited;
	}

	/**
	 * Sets whether the element is inherited.
	 * @param inherited True if the element is inherited.
	 */
	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}
	
	/**
	 * Returns the possibly inherited value.
	 * @return The possibly inherited value.
	 */
	public V getValue() {
		return value;
	}
	
	/**
	 * Sets the possibly inherited value.
	 * @param value The possibly inherited value.
	 */
	public void setValue(V value) {
		this.value = value;
	}
	
}
