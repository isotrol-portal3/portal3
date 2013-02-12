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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Entity that represents an instanced connector.
 * @author Andres Rodriguez
 */
@javax.persistence.Entity
@Table(name = "CONNECTOR")
@NamedQueries({
	@NamedQuery(name = ConnectorEntity.OFFLINE, query = "from ConnectorEntity as e inner join fetch e.current left join fetch e.current.configuration where e.deleted is false and e.environment.id = ?"),
	@NamedQuery(name = ConnectorEntity.PFM, query = "from ConnectorEntity as e where e.everPublished is null")})
public class ConnectorEntity extends PublishableEntity<ConnectorEntity, ConnectorDfn, ConnectorEdition> {
	/** Query: Offline content types. */
	public static final String OFFLINE = "connector.offline";
	/** Query: Published flag migration. */
	public static final String PFM = "connector.pfm";
	/** Current definition. */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CNNT_DFN_ID", nullable = true)
	private ConnectorDfn current;
	/** Definitions. */
	@OneToMany(mappedBy = "entity", fetch = FetchType.LAZY)
	private Set<ConnectorDfn> definitions;

	/** Default constructor. */
	public ConnectorEntity() {
	}

	/**
	 * @see com.isotrol.impe3.pms.model.PublishableEntity#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return "Connector";
	}

	/**
	 * Returns the current definition.
	 * @return The current definition.
	 */
	public ConnectorDfn getCurrent() {
		return current;
	}

	/**
	 * Sets the current definition.
	 * @param current The current definition.
	 */
	public void setCurrent(ConnectorDfn current) {
		this.current = current;
	}

	@Override
	public Set<ConnectorDfn> getDefinitions() {
		return definitions;
	}
}
