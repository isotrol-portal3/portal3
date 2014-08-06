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
 * Entity that represents a Comunity Member.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_MEMBER")
@NamedQueries( {
	@NamedQuery(name = MemberEntity.NOT_DELETED, query = "from MemberEntity as e where e.deleted is false"),
	@NamedQuery(name = MemberEntity.BY_NAME, query = "from MemberEntity as e where e.name = ? and e.deleted is false"),
	@NamedQuery(name = MemberEntity.BY_LASTCODE, query = "from MemberEntity as e where e.lastMemberCode = ?"),
	@NamedQuery(name = MemberEntity.BY_CODE, query = "from MemberEntity as e where e.memberCode = ? and e.deleted is false")})
public class MemberEntity extends VersionedEntity {
	/** Query by not deleted. */
	public static final String NOT_DELETED = "member.notDeleted";
	/** Query by name. */
	public static final String BY_NAME = "member.byName";
	/** Query by external user id. */
	public static final String BY_CODE = "member.byCode";

	/** Query by external user id. */
	public static final String BY_LASTCODE = "member.byLastCode";

	/** Original member id. */
	@Column(name = "MMBR_CODE", length = Lengths.NAME, unique = true, nullable = false)
	private String memberCode;
	/** Login name. */
	@Column(name = "MMBR_NAME", length = Lengths.NAME, unique = true, nullable = false)
	private String name;
	/** Display name. */
	@Column(name = "MMBR_DISPLAY_NAME", length = Lengths.DESCRIPTION)
	private String displayName;

	/** Email address. */
	@Column(name = "MMBR_EMAIL", length = Lengths.NAME)
	private String email;

	/** Creation date. */
	@Column(name = "MMBR_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	/** Membership changes version number. */
	@Column(name = "MMBR_MMSP_VERSION", nullable = false)
	private int membershipVersion = 0;

	/** Whether the user is deleted. */
	@Column(name = "MMBR_DELETED", nullable = false)
	private boolean deleted = false;
	
	/** Whether the user is blocked. */
	@Column(name = "MMBR_BLOCKED", nullable = false)
	private boolean blocked = false;
	

	/** Original member id. */
	@Column(name = "MMBR_LASTCODE", length = Lengths.NAME, unique = false, nullable = true)
	private String lastMemberCode;
	/** Login name. */
	@Column(name = "MMBR_LASTNAME", length = Lengths.NAME, unique = false, nullable = true)
	private String lastName;

	/** Member properties. */
	@CollectionOfElements
	@JoinTable(name = "WEB20_MEMBER_PROPERTY", joinColumns = @JoinColumn(name = "MMBR_ID", nullable = false))
	@MapKey(columns = @Column(name = "MMBR_PROPERTY_NAME", length = Lengths.NAME))
	@Column(name = "MMBR_PROPERTY_VALUE", length = Lengths.DESCRIPTION)
	private Map<String, String> properties;

	/** Member profiles. */
	@CollectionOfElements
	@JoinTable(name = "WEB20_MEMBER_PROFILE", joinColumns = @JoinColumn(name = "MMBR_ID", nullable = false))
	@Column(name = "MMBR_PROFILE_NAME", length = Lengths.NAME)
	private Set<String> profiles;

	/** Memberships. */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private Set<MembershipEntity> memberships;

	
	/** Default constructor. */
	public MemberEntity() {
		super();
	}

	/**
	 * Constructor.
	 * @param id Entity ID.
	 */
	public MemberEntity(UUID id) {
		super(id);
	}

	/**
	 * Returns the member code.
	 * @return The member code.
	 */
	public String getMemberCode() {
		return memberCode;
	}

	/**
	 * Sets the member code
	 * @param memberCode The member code.
	 */
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	/**
	 * Returns the login name.
	 * @return The login name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the login name.
	 * @param name The login name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the display name.
	 * @return The display name.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 * @param displayName The display name.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Returns the email address.
	 * @return The email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address.
	 * @param email The email address.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the creation date.
	 * @return The creation date.
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Sets the creation date.
	 * @param date The creation date.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Returns the membership change version.
	 * @return The membership change version.
	 */
	public int getMembershipVersion() {
		return membershipVersion;
	}

	/**
	 * Sets the membership change version.
	 * @param membershipVersion The membership change version.
	 */
	public void setMembershipVersion(int membershipVersion) {
		this.membershipVersion = membershipVersion;
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
	 * Returns whether the member is blocked.
	 * @return True if the member is blocked.
	 */
	public boolean isBlocked() {
		return blocked;
	}
	
	/**
	 * Sets whether the member is blocked.
	 * @param deleted True if the member is blocked.
	 */
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
	/**
	 * Returns the last member code.
	 * @return The last member code.
	 */
	public String getLastMemberCode() {
		return lastMemberCode;
	}

	/**
	 * Sets the last member code.
	 * @param lastMemberCode The last member code.
	 */
	public void setLastMemberCode(String lastMemberCode) {
		this.lastMemberCode = lastMemberCode;
	}

	/**
	 * Returns the last name.
	 * @return The last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 * @param lastName The last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the member properties.
	 * @return The member properties.
	 */
	public Map<String, String> getProperties() {
		if (properties == null) {
			properties = Maps.newHashMap();
		}
		return properties;
	}

	/**
	 * Returns the member profiles.
	 * @return The member profiles.
	 */
	public Set<String> getProfiles() {
		if (profiles == null) {
			profiles = Sets.newHashSet();
		}
		return profiles;
	}

	/**
	 * Returns the member communities and roles.
	 * @return The member communities and roles.
	 */
	public Set<MembershipEntity> getMemberships() {
		return memberships;
	}


}
