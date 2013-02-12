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


import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;


/**
 * Value that represents a content type mapping.
 * @author Andres Rodriguez
 */
@Embeddable
public class ContentTypeMappingValue implements MappingValue {
	/** Content type. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "COTP_ID", nullable = false)
	private ContentTypeEntity contentType;
	/** Mapping. */
	@Column(name = "MAPPING", length = Lengths.MAX)
	private String mapping;

	/** Default constructor. */
	public ContentTypeMappingValue() {
	}

	/**
	 * @see com.isotrol.impe3.pms.model.MappingValue#getId()
	 */
	public UUID getId() {
		return contentType != null ? contentType.getId() : null;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.MappingValue#isDeleted()
	 */
	public boolean isDeleted() {
		return contentType != null ? contentType.isDeleted() : false;
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public ContentTypeEntity getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * @param contentType The content type.
	 */
	public void setContentType(ContentTypeEntity contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns the mapping.
	 * @return The mapping.
	 */
	public String getMapping() {
		return mapping;
	}

	/**
	 * Sets the mapping.
	 * @param mapping The mapping.
	 */
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return Objects.hashCode(contentType, mapping);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof ContentTypeMappingValue) {
			final ContentTypeMappingValue m = (ContentTypeMappingValue) obj;
			return Objects.equal(contentType, m.contentType) && Objects.equal(mapping, m.mapping);
		}
		return false;
	}

}
