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


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;


/**
 * Entity that represents a source mapping.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "SOURCE_MAPPING")
@NamedQuery(name = SourceMappingEntity.BY_NAME, query = "from SourceMappingEntity as e where e.name = ? and e.environment.id = ?")
public class SourceMappingEntity extends OfEnvironment {
	/** Query by name and enviroment. */
	public static final String BY_NAME = "mapping.byName";
	/** Mapping name. */
	@Column(name = "NAME", length = Lengths.NAME, unique = true, nullable = false)
	private String name;
	/** Mapping description. */
	@Column(name = "DESCRIPTION", length = Lengths.MAX)
	private String description;
	/** Set mappings. */
	@CollectionOfElements
	@JoinTable(name = "SET_MAPPING", joinColumns = @JoinColumn(name = "SRCM_ID"))
	private List<SetMappingValue> sets;
	/** Content type mappings. */
	@CollectionOfElements
	@JoinTable(name = "CONTENT_TYPE_MAPPING", joinColumns = @JoinColumn(name = "SRCM_ID"))
	private List<ContentTypeMappingValue> contentTypes;
	/** Category mappings. */
	@CollectionOfElements
	@JoinTable(name = "CATEGORY_MAPPING", joinColumns = @JoinColumn(name = "SRCM_ID"))
	private List<CategoryMappingValue> categories;

	/** Default constructor. */
	public SourceMappingEntity() {
	}

	/**
	 * Returns the mapping name.
	 * @return The mapping name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the login name.
	 * @param name The login name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the mapping description.
	 * @return The mapping description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the mapping description.
	 * @param description The mapping description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the set mappings.
	 * @return The set mappings.
	 */
	public List<SetMappingValue> getSets() {
		return sets;
	}
	
	/**
	 * Sets the set mappings.
	 * @param sets The set mappings.
	 */
	public void setSets(List<SetMappingValue> sets) {
		this.sets = sets;
	}

	/**
	 * Returns the content type mappings.
	 * @return The content type mappings.
	 */
	public List<ContentTypeMappingValue> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Sets the content type mappings.
	 * @param contentTypes The content type mappings.
	 */
	public void setContentTypes(List<ContentTypeMappingValue> contentTypes) {
		this.contentTypes = contentTypes;
	}

	/**
	 * Returns the category mappings.
	 * @return The category mappings.
	 */
	public List<CategoryMappingValue> getCategories() {
		return categories;
	}

	/**
	 * Sets the category mappings.
	 * @param categories The category mappings.
	 */
	public void setCategories(List<CategoryMappingValue> categories) {
		this.categories = categories;
	}
}
