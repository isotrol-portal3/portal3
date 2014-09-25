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

package com.isotrol.impe3.mappings;


import java.io.Serializable;
import java.util.List;


/**
 * DTO for a source mapping.
 * @author Andres Rodriguez
 */
public class MappingsDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 5898003504474010217L;
	/** Mappings version. */
	private int version;
	/** Content types. */
	private List<MappingDTO> contentTypes;
	/** Categories. */
	private List<MappingDTO> categories;
	/** Sets. */
	private List<MappingDTO> sets;

	/** Default constructor. */
	public MappingsDTO() {
	}

	/**
	 * Returns the source mapping version.
	 * @return The source mapping version.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the source mapping version.
	 * @param version The source mapping version.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Returns the content type mappings.
	 * @return The content type mappings.
	 */
	public List<MappingDTO> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Sets the content type mappings.
	 * @param contentTypes The content type mappings.
	 */
	public void setContentTypes(List<MappingDTO> contentTypes) {
		this.contentTypes = contentTypes;
	}

	/**
	 * Returns the category mappings.
	 * @return The category mappings.
	 */
	public List<MappingDTO> getCategories() {
		return categories;
	}

	/**
	 * Sets the category mappings.
	 * @param categories The category mappings.
	 */
	public void setCategories(List<MappingDTO> categories) {
		this.categories = categories;
	}
	
	/**
	 * Returns the sets mappings.
	 * @return The sets mappings.
	 */
	public List<MappingDTO> getSets() {
		return sets;
	}
	
	/**
	 * Sets the sets mappings.
	 * @param sets The sets mappings.
	 */
	public void setSets(List<MappingDTO> sets) {
		this.sets = sets;
	}
}
