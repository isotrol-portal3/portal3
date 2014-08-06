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
package com.isotrol.impe3.web20.server;

import java.io.Serializable;

import static java.lang.Math.max;

/**
 * 
 * @author Emilio Escobar Reyero
 */
public class LogTableFilterDTO implements Serializable {
	/** serial uid */
	private static final long serialVersionUID = 8361767505485431599L;

	/** Default page size. */
	public static final int SIZE = 100;
	
	/** the name. */
	private String name;

	/** First record. */
	private long first;
	/** Page size. */
	private int size = -1;

	
	/** Constructor */
	public LogTableFilterDTO() {
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
	 * Returs the first record.
	 * @return The first record.
	 */
	public long getFirst() {
		return first;
	}
	
	/**
	 * Sets the first record.
	 * @param first The first record.
	 */
	public void setFirst(long first) {
		this.first = max(0,first);
	}
	
	/**
	 * Returns the size.
	 * @return The size.
	 */
	public int getSize() {
		return size > 0 ? size : SIZE;
	}
	
	/**
	 * Sets the size.
	 * @param size The size.
	 */
	public void setSize(int size) {
		this.size = max(0, size);
	}
}
