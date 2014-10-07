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

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.collect.Maps;
import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.es.common.model.VersionedLongIdEntity;
import com.isotrol.impe3.hib.model.Lengths;


/**
 * Entity that represents a community member.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_MEMBERSHIP")
@NamedQueries( {
	@NamedQuery(name = MembershipEntity.MC, query = "from MembershipEntity as e where e.member.id = ? and e.community.id = ? and e.deletion is null"),
	@NamedQuery(name = MembershipEntity.MCD, query = "from MembershipEntity as e where e.member.id = ? and e.community.id = ?"),
	@NamedQuery(name = MembershipEntity.VMC, query = "from MembershipEntity as e where e.member.id = ? and e.community.id = ? and e.validation is not null and e.deletion is null"),
	@NamedQuery(name = MembershipEntity.COUNT_VM, query = "select e.community.id, count(e.id) from MembershipEntity as e where e.deletion is null and e.validation is not null group by e.community.id"),
	@NamedQuery(name = MembershipEntity.COUNT_NVM, query = "select e.community.id, count(e.id) from MembershipEntity as e where e.deletion is null and e.validation is null group by e.community.id")})
public class MembershipEntity extends VersionedLongIdEntity {
	/** Query by member and community. */
	public static final String MC = "membership.mc";
	/** Query by member and community. */
	public static final String MCD = "membership.mcd";
	/** Query by member and community (only validated memberships). */
	public static final String VMC = "membership.vmc";
	/** Count validated members. */
	public static final String COUNT_VM = "membership.countVM";
	/** Count non-validated members. */
	public static final String COUNT_NVM = "membership.countNVM";

	/** Member. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "MMSP_MMBR_ID", nullable = false)
	private MemberEntity member;

	/** Community. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "MMSP_CMTY_ID", nullable = false)
	private CommunityEntity community;

	/** Member role on community. */
	@Column(name = "MMSP_ROLE", nullable = false)
	private String role;

	/** Request creation date. */
	@Column(name = "MMSP_REQUEST", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar request;
	/** Validation date. */
	@Column(name = "MMSP_VALIDATION", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar validation;
	/** Deletion date. */
	@Column(name = "MMSP_DELETION", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar deletion;

	/** Membership properties. */
	@ElementCollection
	@JoinTable(name = "WEB20_MEMBERSHIP_PROPERTY", joinColumns = @JoinColumn(name = "MMSP_ID", nullable = false))
	@MapKeyColumn(name = "MMSP_PROPERTY_NAME", length = Lengths.NAME)
	@Column(name = "MMSP_PROPERTY_VALUE", length = Lengths.DESCRIPTION)
	private Map<String, String> properties;
	
	/** Default constructor. */
	public MembershipEntity() {
		super();
	}

	/**
	 * Returns the member.
	 * @return The member.
	 */
	public MemberEntity getMember() {
		return member;
	}

	/**
	 * Sets the member.
	 * @param member The member.
	 */
	public void setMember(MemberEntity member) {
		this.member = member;
	}

	/**
	 * Returns the community.
	 * @return The community.
	 */
	public CommunityEntity getCommunity() {
		return community;
	}

	/**
	 * Sets the community.
	 * @param community The community.
	 */
	public void setCommunity(CommunityEntity community) {
		this.community = community;
	}

	/**
	 * Returns the member role on community.
	 * @return The member role on community.
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the member role on community.
	 * @param role The member role on community.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Returns the request date.
	 * @return The request date.
	 */
	public Calendar getRequest() {
		return request;
	}

	/**
	 * Sets the request date.
	 * @param request The request date.
	 */
	public void setRequest(Calendar request) {
		this.request = request;
	}

	/**
	 * Returns the membership validation date.
	 * @return The membership validation date.
	 */
	public Calendar getValidation() {
		return validation;
	}

	/**
	 * Sets the membership validation date.
	 * @param validation The membership validation date.
	 */
	public void setValidation(Calendar validation) {
		this.validation = validation;
	}

	/**
	 * Returns the membership deletion date.
	 * @return The membership deletion date.
	 */
	public Calendar getDeletion() {
		return deletion;
	}

	/**
	 * Sets the membership deletion date.
	 * @param deletion The membership deletion date.
	 */
	public void setDeletion(Calendar deletion) {
		this.deletion = deletion;
	}

	/**
	 * Returns the membership properties.
	 * @return The membership properties.
	 */
	public Map<String, String> getProperties() {
		if (properties == null) {
			properties = Maps.newHashMap();
		}
		return properties;
	}
	
	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return MembershipEntity.class;
	}

}
