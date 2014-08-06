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
 * Entity that represents a Comment moderation.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_CMMT_MODERATION")
public class CommentModerationEntity extends LongIdEntity {

	/** Comment. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CMTM_CMMT_ID", nullable = false)
	private CommentEntity comment;

	/** Moderator code. */
	@Column(name = "CMTM_MODERATOR", length = Lengths.NAME, nullable = true)
	private String moderator;

	/** Moderation result. */
	@Column(name = "CMTM_MODERATED", nullable = false)
	private Boolean moderated;

	/** Timestamp. */
	@Column(name = "CMTM_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	/** Default constructor. */
	public CommentModerationEntity() {
	}
	
	/**
	 * Returns the comment.
	 * @return The comment.
	 */
	public CommentEntity getComment() {
		return comment;
	}

	// TODO needed?
	public void setComment(CommentEntity comment) {
		this.comment = comment;
	}

	/**
	 * Returns the moderator code.
	 * @return The moderator code.
	 */
	public String getModerator() {
		return moderator;
	}

	/**
	 * Sets moderator code.
	 * @param moderator The moderator code.
	 */
	public void setModerator(String moderator) {
		this.moderator = moderator;
	}

	/**
	 * Returns whether the comment is moderated.
	 * @return True if the comment is moderated.
	 */
	public boolean isModerated() {
		return moderated;
	}

	/**
	 * Sets whether the comment is moderated.
	 * @param deleted True if the comment is moderated.
	 */
	public void setModerated(Boolean moderated) {
		this.moderated = moderated;
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

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return CommentModerationEntity.class;
	}

}
