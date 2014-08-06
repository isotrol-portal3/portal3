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


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.es.common.model.VersionedLongIdEntity;


/**
 * Entity that represents a Resource.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "WEB20_RESOURCE")
@NamedQuery(name = ResourceEntity.BY_ID, query = "from ResourceEntity as e where e.resourceId = ?")
public class ResourceEntity extends VersionedLongIdEntity {
	/** Query by resource id. */
	public static final String BY_ID = "resource.byId";
	/** Not deleted predicate. */
	public static final Predicate<ResourceEntity> NOT_DELETED = new Predicate<ResourceEntity>() {
		public boolean apply(ResourceEntity input) {
			return !input.isDeleted();
		}
	};

	/** Original resource id. */
	@Column(name = "RSRC_RESOURCE_ID", length = 640, unique = true, nullable = false, updatable = false)
	private String resourceId;

	/** Whether the resource is deleted. */
	@Column(name = "RSRC_DELETED", nullable = false)
	private boolean deleted = false;

	/** Communities. */
	@OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
	private Set<RelationshipEntity> relationships;

	/** Tags. */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "WEB20_RESOURCE_TAG", joinColumns = {@JoinColumn(name = "RSRC_ID")}, inverseJoinColumns = {@JoinColumn(name = "TAGT_ID")})
	private Set<TagEntity> tags;

	/** Constructor. */
	public ResourceEntity() {
	}

	/**
	 * Returns the resource id.
	 * @return The resource id.
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * Sets the resource id.
	 * @param resourceId The resource id.
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * Returns the resource relationships.
	 * @return The resource relationships.
	 */
	public Set<RelationshipEntity> getRelationships() {
		return relationships;
	}

	/**
	 * Returns whether the member is deleted.
	 * @return True if the member is deleted.
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets whether the member is deleted.
	 * @param deleted True if the member is deleted.
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Returns the resource tags.
	 * @return The resource tags.
	 */
	public Set<TagEntity> getTags() {
		if (tags == null) {
			tags = Sets.newHashSet();
		}
		return tags;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return ResourceEntity.class;
	}

}
