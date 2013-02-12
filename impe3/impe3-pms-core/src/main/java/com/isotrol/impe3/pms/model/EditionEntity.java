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


import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Entity that represents an edition.
 * @author Andres Rodriguez
 */
@javax.persistence.Entity
@Table(name = "EDITION")
@NamedQueries({
	@NamedQuery(name = EditionEntity.BY_ID, query = "from EditionEntity as e where e.id = ? and e.environment.id = ?"),
	@NamedQuery(name = EditionEntity.LASTEST, query = "from EditionEntity as e where e.environment.id = ?")})
public class EditionEntity extends OfEnvironment {
	/** Query by ID. */
	public static final String BY_ID = "edition.byId";
	/** Query lastest. */
	public static final String LASTEST = "edition.lastest";
	/** Last publication record. */
	private LastPublished lastPublished;

	@OneToMany(mappedBy = "edition", fetch = FetchType.LAZY)
	private Set<ContentTypeEdition> contentTypes;
	/** Categories. */
	@OneToMany(mappedBy = "edition", fetch = FetchType.LAZY)
	private Set<CategoryEdition> categories;
	/** Connectors. */
	@OneToMany(mappedBy = "edition", fetch = FetchType.LAZY)
	private Set<ConnectorEdition> connectors;
	/** Portals. */
	@OneToMany(mappedBy = "edition", fetch = FetchType.LAZY)
	private Set<PortalEdition> portals;

	/** Default constructor. */
	public EditionEntity() {
	}

	/**
	 * Returns the last publication record.
	 * @return The last publication record.
	 */
	public LastPublished getLastPublished() {
		return lastPublished;
	}

	/**
	 * Sets the last publication record.
	 * @param lastPublished The last publication record.
	 */
	public void setLastPublished(LastPublished lastPublished) {
		this.lastPublished = lastPublished;
	}

	/**
	 * Sets the last publication record.
	 * @param user User performing the publication.
	 */
	public void setLastPublished(UserEntity user) {
		this.lastPublished = new LastPublished(user);
	}

	/**
	 * Returns the content types.
	 * @return The content types.
	 */
	public Set<ContentTypeEdition> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Returns the categories.
	 * @return The categories.
	 */
	public Set<CategoryEdition> getCategories() {
		return categories;
	}

	/**
	 * Returns the connectors.
	 * @return The connectors.
	 */
	public Set<ConnectorEdition> getConnectors() {
		return connectors;
	}

	/**
	 * Returns the portals.
	 * @return The portals.
	 */
	public Set<PortalEdition> getPortals() {
		return portals;
	}

}
