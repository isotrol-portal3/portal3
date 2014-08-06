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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.es.common.model.VersionedLongIdEntity;
import com.isotrol.impe3.hib.model.Lengths;


/**
 * Entity that represents a Community Notice.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_NOTICE")
public class NoticeEntity extends VersionedLongIdEntity {

	/** Community. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "NTCE_CMTY_ID", nullable = false)
	private CommunityEntity community;

	/** Member. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "NTCE_MMBR_ID", nullable = false)
	private MemberEntity member;

	/** Timestamp. */
	@Column(name = "NTCE_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	/** Notice description. */
	@Column(name = "NTCE_DESCRIPTION", length = Lengths.DESCRIPTION, unique = false, nullable = false)
	private String description;

	/** Whether the community notice is deleted. */
	@Column(name = "NTCE_DELETED", nullable = true)
	private Boolean deleted = false;

	
	/** Notice title. */
	@Column(name = "NTCE_TITLE", length = Lengths.TITLE, unique = false, nullable = false)
	private String title;
	
	/** Release date. */
	@Column(name = "NTCE_RELEASE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar release;

	/** Expiration date. */
	@Column(name = "NTCE_EXPIRATION", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar expiration;

	
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
	 * Returns the date.
	 * @return The date.
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * @param date The date.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Returns the community notice description.
	 * @return The community notice description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the community report description.
	 * @param description The community report description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns whether the notice is deleted.
	 * @return True if the notice is deleted.
	 */
	public boolean isDeleted() {
		return deleted != null ? deleted.booleanValue() : false;
	}

	/**
	 * Sets whether the notice is deleted.
	 * @param deleted True if notice is deleted.
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Returns the title.
	 * @return The title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title.
	 * @param title The title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the expiration date.
	 * @return The expiration date.
	 */
	public Calendar getExpiration() {
		return expiration;
	}
	
	/**
	 * Sets the expiration date.
	 * @param expiration The expiration date.
	 */
	public void setExpiration(Calendar expiration) {
		this.expiration = expiration;
	}
	
	/**
	 * Returns the release date.
	 * @return The release date.
	 */
	public Calendar getRelease() {
		return release;
	}
	
	/**
	 * Sets the release date.
	 * @param release The release date.
	 */
	public void setRelease(Calendar release) {
		this.release = release;
	}
	
	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return NoticeEntity.class;
	}

}
