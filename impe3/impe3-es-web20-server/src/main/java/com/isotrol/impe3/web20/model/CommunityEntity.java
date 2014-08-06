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


import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKey;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.hib.model.Lengths;
import com.isotrol.impe3.hib.model.VersionedEntity;


/**
 * Entity that represents a Comunity (like groups).
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_COMMUNITY")
@NamedQueries( {
	@NamedQuery(name = CommunityEntity.NOT_DELETED, query = "from CommunityEntity as e where e.deleted is false"),
	@NamedQuery(name = CommunityEntity.BY_LASTCODE, query = "from CommunityEntity as e where e.lastCode = ?"),
	@NamedQuery(name = CommunityEntity.BY_CODE, query = "from CommunityEntity as e where e.code = ? and e.deleted is false")})
public class CommunityEntity extends VersionedEntity {
	/** Query by not deleted. */
	public static final String NOT_DELETED = "community.notDeleted";
	/** Query by code name. */
	public static final String BY_CODE = "community.byCode";
	/** Query by code name. */
	public static final String BY_LASTCODE = "community.byLastCode";

	/** Community code name. */
	@Column(name = "CMTY_CODE", length = Lengths.NAME, unique = true, nullable = false)
	private String code;

	/** Community name. */
	@Column(name = "CMTY_NAME", length = Lengths.NAME, unique = false, nullable = false)
	private String name;

	/** Description. */
	@Column(name = "CMTY_DESCRIPTION", length = Lengths.DESCRIPTION, nullable = true)
	private String description;

	/** Creation date. */
	@Column(name = "CMTY_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	/** Whether the community is deleted. */
	@Column(name = "CMTY_DELETED", nullable = true)
	private boolean deleted = false;

	/** Community code name. */
	@Column(name = "CMTY_LASTCODE", length = Lengths.NAME, unique = false, nullable = true)
	private String lastCode;

	/** Members. */
	@OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
	private Set<MembershipEntity> memberships;

	/** Community properties. */
	@CollectionOfElements
	@JoinTable(name = "WEB20_COMMUNITY_PROPERTY", joinColumns = @JoinColumn(name = "CMTY_ID", nullable = false))
	@MapKey(columns = @Column(name = "CMTY_PROPERTY_NAME", length = Lengths.NAME))
	@Column(name = "CMTY_PROPERTY_VALUE", length = Lengths.DESCRIPTION)
	private Map<String, String> properties;
	
	
	/** Default constructor. */
	public CommunityEntity() {
		super();
	}

	/**
	 * Constructor.
	 * @param id Entity ID.
	 */
	public CommunityEntity(UUID id) {
		super(id);
	}

	/**
	 * Returns the community name.
	 * @return The community name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the community name.
	 * @param name The community name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the community code name.
	 * @return The community code name.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the community code name.
	 * @param code The community code name.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Returns the community description.
	 * @return The community description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the community description.
	 * @param description The community description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the community creation date.
	 * @return The community creation date.
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Sets the community creation date.
	 * @param date The community creation date.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Returns whether the community is deleted.
	 * @return True if the community is deleted.
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets whether the community is deleted.
	 * @param deleted True if the community is deleted.
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Returns the last code.
	 * @return The last code.
	 */
	public String getLastCode() {
		return lastCode;
	}

	/**
	 * Sets the last code.
	 * @param lastCode The last code.
	 */
	public void setLastCode(String lastCode) {
		this.lastCode = lastCode;
	}

	/**
	 * Returns the community members and roles.
	 * @return The community members and roles.
	 */
	public Set<MembershipEntity> getMemberships() {
		if (memberships == null) {
			memberships = Sets.newHashSet();
		}
		return memberships;
	}
	
	/**
	 * Returns the community properties.
	 * @return The community properties.
	 */
	public Map<String, String> getProperties() {
		if (properties == null) {
			properties = Maps.newHashMap();
		}
		return properties;
	}
}
