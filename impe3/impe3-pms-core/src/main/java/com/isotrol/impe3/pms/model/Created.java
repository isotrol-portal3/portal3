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


import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;


/**
 * Value that represents the creation record.
 * @author Andres Rodriguez
 */
@Embeddable
public class Created implements Done {
	/** User. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY", nullable = false)
	private UserEntity user;
	/** Timestamp. */
	@Column(name = "CREATED", nullable = false)
	private Calendar timestamp;

	/** Default constructor. */
	public Created() {
	}

	/**
	 * Default constructor.
	 * @param user User.
	 */
	public Created(UserEntity user) {
		this.user = user;
		this.timestamp = Calendar.getInstance();
	}

	/**
	 * @see com.isotrol.impe3.pms.model.Done#getUser()
	 */
	public UserEntity getUser() {
		return user;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.Done#setUser(com.isotrol.impe3.pms.model.UserEntity)
	 */
	public void setUser(UserEntity user) {
		this.user = user;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.Done#getTimestamp()
	 */
	public Calendar getTimestamp() {
		return timestamp;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.Done#setTimestamp(java.util.Calendar)
	 */
	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(user, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Created) {
			final Created c = (Created) obj;
			return Objects.equal(user, c.user) && Objects.equal(timestamp, c.timestamp);
		}
		return false;
	}
}
