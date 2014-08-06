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
import com.isotrol.impe3.hib.model.Lengths;


/**
 * Entity that represents a Resource marked as Favorite.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_FAVORITE")
public class FavoriteEntity extends LongIdEntity {

	/** Member. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "FVRT_MMBR_ID", nullable = false)
	private MemberEntity member;
	/** Resource. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "FVRT_RLTS_ID", nullable = false)
	private RelationshipEntity relationship;

	/** Timestamp. */
	@Column(name = "FVRT_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	/** Description. */
	@Column(name = "FVRT_DESC", length = Lengths.DESCRIPTION, nullable = true)
	private String description;
	
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
	 * Returns the resource.
	 * @return The resource.
	 */
	public RelationshipEntity getRelationship() {
		return relationship;
	}

	/**
	 * Sets the resource.
	 * @param resource The resource.
	 */
	public void setRelationship(RelationshipEntity relationship) {
		this.relationship = relationship;
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
	 * Returns the description.
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * @param description The description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return FavoriteEntity.class;
	}

}
