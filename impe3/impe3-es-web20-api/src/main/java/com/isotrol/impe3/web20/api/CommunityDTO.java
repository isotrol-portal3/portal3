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


import java.util.Date;
import java.util.Map;

import com.isotrol.impe3.dto.AbstractVersionedStringId;


/**
 * DTO for community detail.
 * @author Emilio Escobar Reyero
 */
public class CommunityDTO extends AbstractVersionedStringId {
	/** Serial UID. */
	private static final long serialVersionUID = 921445847812876416L;

	/** Community code. */
	private String code;

	/** Community name. */
	private String name;

	/** Community description. */
	private String description;

	/** Community creation date. */
	private Date date;

	/** Number of validated members. */
	private int validatedMembers;

	/** Number of non-validated members. */
	private int nonValidatedMembers;

	/** Community properties. */
	private Map<String, String> properties;
	
	/** Default constructor. */
	public CommunityDTO() {
	}

	/**
	 * Returns the community code.
	 * @return The community code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the community code.
	 * @param code The community code.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Returns the community name.
	 * @return The community name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the community name.
	 * @param name The community name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the community description.
	 * @return The community description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the community description.
	 * @param description The description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the community creation date.
	 * @return The community creation date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the community creation date.
	 * @param date The community creation date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Returns the number of validated members.
	 * @return The number of validated members.
	 */
	public int getValidatedMembers() {
		return validatedMembers;
	}

	/**
	 * Sets the number of validated members.
	 * @param validatedMembers The number of validated members.
	 */
	public void setValidatedMembers(int validatedMembers) {
		this.validatedMembers = validatedMembers;
	}

	/**
	 * Returns the number of non-validated members.
	 * @return The number of non-validated members.
	 */
	public int getNonValidatedMembers() {
		return nonValidatedMembers;
	}

	/**
	 * Sets the number of non-validated members.
	 * @param nonValidatedMembers The number of non-validated members.
	 */
	public void setNonValidatedMembers(int nonValidatedMembers) {
		this.nonValidatedMembers = nonValidatedMembers;
	}

	/**
	 * Returns the community properties.
	 * @return The community properties.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	
	/**
	 * Sets the community properties.
	 * @param properties The community properties.
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
}
