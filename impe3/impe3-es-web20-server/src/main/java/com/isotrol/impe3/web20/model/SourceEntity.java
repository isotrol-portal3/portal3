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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.isotrol.impe3.es.common.model.LongIdEntity;


/**
 * Entity that represents a participation source.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "WEB20_SOURCE")
@NamedQueries({
	@NamedQuery(name = SourceEntity.MEMBER, query = "from SourceEntity as e where e.member.id = ? and e.origin is null"),
	@NamedQuery(name = SourceEntity.ORIGIN, query = "from SourceEntity as e where e.member is null and e.origin = ?")})
public class SourceEntity extends LongIdEntity {
	/** Query by member. */
	public static final String MEMBER = "source.member";
	/** Query by origin. */
	public static final String ORIGIN = "source.origin";

	/** Member. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "SRCE_MMBR_ID", nullable = true)
	private MemberEntity member;

	/** Origin name. */
	@Column(name = "SRCE_ORIGIN", length = 512, unique = false, nullable = true)
	private String origin;

	/** Constructor. */
	public SourceEntity() {
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
	 * Returns the origin.
	 * @return The origin.
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Sets the origin.
	 * @param origin The origin.
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return SourceEntity.class;
	}

}
