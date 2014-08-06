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
package com.isotrol.impe3.web20.model;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.isotrol.impe3.es.common.model.LongIdEntity;


/**
 * Entity that represents a resource and community association.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_RELATIONSHIP")
@Immutable
@NamedQueries( {
	@NamedQuery(name = RelationshipEntity.WG, query = "from RelationshipEntity as e where e.resource.id = ? and e.group.id = ?"),
	@NamedQuery(name = RelationshipEntity.WOG, query = "from RelationshipEntity as e where e.resource.id = ? and e.group is null")})
public class RelationshipEntity extends LongIdEntity {
	/** Query with group. */
	public static final String WG = "relationship.withGroup";
	/** Query without group. */
	public static final String WOG = "relationship.withoutGroup";

	/** Resource. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "RLTS_RSRC_ID", nullable = false)
	private ResourceEntity resource;

	/** Group (optional). */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "RLTS_GRUP_ID", nullable = true)
	private GroupEntity group;

	/** Constructor. */
	public RelationshipEntity() {
	}

	/**
	 * Returns the resource.
	 * @return The resource.
	 */
	public ResourceEntity getResource() {
		return resource;
	}

	/**
	 * Sets the resource.
	 * @param resource The resource.
	 */
	public void setResource(ResourceEntity resource) {
		this.resource = resource;
	}

	/**
	 * Returns the group.
	 * @return The group.
	 */
	public GroupEntity getGroup() {
		return group;
	}

	/**
	 * Sets the group
	 * @param group The group.
	 */
	public void setGroup(GroupEntity group) {
		this.group = group;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return RelationshipEntity.class;
	}

}
