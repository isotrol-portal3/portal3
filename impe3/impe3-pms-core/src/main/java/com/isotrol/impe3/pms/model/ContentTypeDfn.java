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


import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Maps;


/**
 * Content type definition.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "CONTENT_TYPE_DFN")
public class ContentTypeDfn extends AbstractIAEDfn<ContentTypeDfn, ContentTypeEntity, ContentTypeEdition> {
	/** Content type. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "COTP_ID", nullable = false)
	private ContentTypeEntity entity;
	/** Content type description. */
	@Column(name = "DESCRIPTION", length = Lengths.DESCRIPTION, nullable = true)
	private String description;
	/** Localized names. */
	@ElementCollection
	@JoinTable(name = "CONTENT_TYPE_L7D", joinColumns = @JoinColumn(name = "COTP_ID", nullable = false))
	@MapKeyColumn(name = "LOCALE", length = Lengths.LOCALE)
	private Map<String, NameValue> localizedNames;
	/** Whether the content type is navigable. */
	@Column(name = "NAVIGABLE", nullable = true)
	private Boolean navigable = Boolean.TRUE;
	/** Editions. */
	@OneToMany(fetch=FetchType.LAZY, mappedBy="published")
	private Set<ContentTypeEdition> editions;

	/** Default constructor. */
	public ContentTypeDfn() {
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public ContentTypeEntity getEntity() {
		return entity;
	}

	/**
	 * Sets the content type.
	 * @param entity The content type.
	 */
	public void setEntity(ContentTypeEntity entity) {
		this.entity = entity;
	}
	
	/**
	 * @see com.isotrol.impe3.pms.model.Definition#getEditions()
	 */
	@Override
	public Set<ContentTypeEdition> getEditions() {
		return editions;
	}

	/**
	 * Returns the content type description.
	 * @return The content type description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the content type description.
	 * @param description The content type description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the localized names.
	 * @return The localized names.
	 */
	public Map<String, NameValue> getLocalizedNames() {
		if (localizedNames == null) {
			localizedNames = Maps.newHashMap();
		}
		return localizedNames;
	}

	/**
	 * Returns whether the content type is navigable.
	 * @return True if the content type is navigable.
	 */
	public boolean isNavigable() {
		return navigable != null ? navigable.booleanValue() : true;
	}

	/**
	 * Sets whether the content type is navigable.
	 * @param navigable True if the content type is navigable.
	 */
	public void setNavigable(boolean navigable) {
		this.navigable = navigable;
	}

}
