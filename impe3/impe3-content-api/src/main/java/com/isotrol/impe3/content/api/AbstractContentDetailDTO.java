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

package com.isotrol.impe3.content.api;


import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Abstract base DTO for a content resource detail.
 * @author Andres Rodriguez Chamorro
 */
public abstract class AbstractContentDetailDTO extends ContentDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 4369804575182251878L;
	/** Content description. */
	private String description;
	/** Content categories. */
	private Set<CategoryRefPathDTO> categories;
	/** Release date */
	private Date releaseDate;
	/** Expiration date */
	private Date expirationDate;
	/** Content MIME type. */
	private String mime;
	/** Additional properties. */
	private Map<String, List<String>> properties;

	/** Default constructor. */
	public AbstractContentDetailDTO() {
	}

	/**
	 * Returns the content description.
	 * @return The content description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the content description.
	 * @param name The content description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the content categories.
	 * @return The content categories.
	 */
	public Set<CategoryRefPathDTO> getCategories() {
		if (categories == null) {
			categories = new HashSet<CategoryRefPathDTO>();
		}
		return categories;
	}

	/**
	 * Sets the content categories.
	 * @param categories The content categories.
	 */
	public void setCategories(Set<CategoryRefPathDTO> categories) {
		this.categories = categories;
	}

	/**
	 * Returns the content release date.
	 * @return The content release date.
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * Sets the content release date.
	 * @param releaseDate The content release date.
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * Returns the content expiration date.
	 * @return The content expiration date.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Sets the content expiration date.
	 * @param expirationDate The content expiration date.
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Returns the content MIME type.
	 * @return The content MIME type.
	 */
	public String getMime() {
		return mime;
	}

	/**
	 * Sets the content content MIME type.
	 * @param mime The content content MIME type.
	 */
	public void setMime(String mime) {
		this.mime = mime;
	}

	/**
	 * Returns the content properties.
	 * @return The content properties (never {@code null}).
	 */
	public Map<String, List<String>> getProperties() {
		if (properties == null) {
			properties = new HashMap<String, List<String>>();
		}
		return properties;
	}

	/**
	 * Sets the content properties.
	 * @param name The content properties.
	 */
	public void setProperties(Map<String, List<String>> properties) {
		this.properties = properties;
	}
}
