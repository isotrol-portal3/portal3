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

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * Entity that represents and content type edition.
 * @author Andres Rodriguez
 */
@javax.persistence.Entity
@Table(name = "CONTENT_TYPE_EDITION")
@NamedQuery(name = ContentTypeEdition.CURRENT, query = "select entities.published.id from EnvironmentEntity e inner join e.current inner join e.current.contentTypes as entities where e.id = ?")
public class ContentTypeEdition extends PublishedEntity {
	/** Current edition definitions. */
	public static final String CURRENT = "contentTypeEdition.current";
	/** Current definition. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "COTP_DFN_ID", nullable = false)
	private ContentTypeDfn published;

	/** Default constructor. */
	public ContentTypeEdition() {
	}

	/**
	 * Returns the published definition.
	 * @return The published definition.
	 */
	public ContentTypeDfn getPublished() {
		return published;
	}

	/**
	 * Sets the published definition.
	 * @param published The published definition.
	 */
	public void setPublished(ContentTypeDfn published) {
		this.published = published;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.Published#getDefinitionId()
	 */
	public UUID getDefinitionId() {
		return published.getId();
	}

	/**
	 * @see com.isotrol.impe3.pms.model.Published#getEntityId()
	 */
	public UUID getEntityId() {
		return published.getEntity().getId();
	}
}
