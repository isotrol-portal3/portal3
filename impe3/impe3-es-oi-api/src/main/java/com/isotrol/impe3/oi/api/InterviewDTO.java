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
package com.isotrol.impe3.oi.api;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * DTO for interview.
 * @author Emilio Escobar Reyero
 */
public class InterviewDTO extends InterviewSelDTO {
	/** serial uid */
	private static final long serialVersionUID = -445285287328573433L;

	/** Interview content. */
	private String description;

	/** Insertion date. */
	private Date date;
	
	/** Classifications map. */
	private Map<String, Set<String>> classes;
	
	/** Properties. */
	private Map<String, String> properties;
	
	/** Constructor. */
	public InterviewDTO() {
		super();
	}
	
	/**
	 * Returns the description.
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description. 
	 * @param description The description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the date.
	 * @return The date.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the date.
	 * @param date The date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Returns the classification map of class.
	 * @return The classification map of class.
	 */
	public Map<String, Set<String>> getClasses() {
		return classes;
	}
	
	/**
	 * Sets the classification map of class.
	 * @param classes The classification map of class.
	 */
	public void setClasses(Map<String, Set<String>> classes) {
		this.classes = classes;
	}
	
	/**
	 * Returns the interview properties.
	 * @return The interview properties.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	
	/**
	 * Sets the interview properties.
	 * @param properties The interview properties.
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
