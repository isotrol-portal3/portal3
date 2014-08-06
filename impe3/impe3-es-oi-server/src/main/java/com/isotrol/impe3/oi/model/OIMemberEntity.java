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
package com.isotrol.impe3.oi.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.common.base.Predicate;
import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.es.common.model.VersionedLongIdEntity;
import com.isotrol.impe3.hib.model.Lengths;

/**
 * Entity that represents a member relation.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "OI_MEMBER")
@NamedQueries( {
	@NamedQuery(name = OIMemberEntity.BY_MEMBERID, query = "from OIMemberEntity as e where e.memberId = ?")})
public class OIMemberEntity extends VersionedLongIdEntity {
	/** Query by member uuid. */
	public static final String BY_MEMBERID = "oimember.byMemberId";
	
	/** Not deleted predicate. */
	public static final Predicate<OIMemberEntity> NOT_DELETED = new Predicate<OIMemberEntity>() {
		public boolean apply(OIMemberEntity input) {
			return !input.isDeleted();
		}
	};
	
	/** Member uuid. */
	@Column(name = "MMBR_MEMBER_ID", length = Lengths.UUID, nullable = false)
	private String memberId;

	/** Display name. */
	@Column(name = "MMBR_DISPLAY_NAME", length = Lengths.DESCRIPTION)
	private String displayName;

	/** Whether the resource is deleted. */
	@Column(name = "MMBR_DELETED", nullable = false)
	private boolean deleted = false;
	
	/** constructor. */
	public OIMemberEntity() {
		super();
	}
	
	/**
	 * Returns the member uuid.
	 * @return The member uuid.
	 */
	public String getMemberId() {
		return memberId;
	}
	
	/**
	 * Sets the member uuid.
	 * @param memberId The member uuid.
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	
	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return OIMemberEntity.class;
	}
}
