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
package com.isotrol.impe3.tickets.server.model;


import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.isotrol.impe3.hib.model.Lengths;
import com.isotrol.impe3.hib.model.VersionedEntity;


/**
 * Entity that represents a ticket subject.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "IMPE3_TICKET_SUBJECT")
@NamedQuery(name = SubjectEntity.BY_NAME, query = "from SubjectEntity as e where e.name = ?")
public class SubjectEntity extends VersionedEntity {
	/** Query by name. */
	public static final String BY_NAME = "subject.byName";
	/** Query by not moderated. */
	public static final String NOT_MODERATED = "comment.notModerated";
	/** Query by valid (true moderation). */
	public static final String VALID = "comment.isValid";
	/** All (for counting). */
	public static final String ALL = "comment.all";
	/** All (for time-based counting). */
	public static final String ALL_TIMED = "comment.allTimed";
	/** Resources with unmoderated comments. */
	public static final String RWUC = "comment.rwuc";
	/** Resources with unmoderated comments in a community. */
	public static final String RWUC_IC = "comment.rwuc_ic";

	/** Subject name. */
	@Column(name = "SUBJ_NAME", length = Lengths.TITLE, unique = true, nullable = false)
	private String name;

	/** Creation timestamp. */
	@Column(name = "SUBJ_CREATED", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date = Calendar.getInstance();

	/** Default constructor. */
	public SubjectEntity() {
	}

	/**
	 * Returns the subject name.
	 * @return The subject name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the subject name.
	 * @param name The subject name.
	 */
	public void setName(String name) {
		this.name = name;
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

}
