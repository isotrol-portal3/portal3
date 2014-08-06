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


/**
 * Entity that represents a Comment rate.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_CMMT_RATE")
public class CommentRateEntity extends LongIdEntity {

	/** Comment. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "RATE_CMMT_ID", nullable = false)
	private CommentEntity comment;

	/** Member code. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "RATE_SRCE_ID", nullable = false)
	private SourceEntity source;

	/** Rate value. */
	@Column(name = "RATE_VALUE", nullable = false)
	private Double rate;

	/** Timestamp. */
	@Column(name = "RATE_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	/** Default constructor. */
	public CommentRateEntity() {
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
	 * Returns the rate value.
	 * @return The rate value.
	 */
	public Double getRate() {
		return rate;
	}
	
	/**
	 * Sets the rate value.
	 * @param rate The rate value.
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	/**
	 * Returns the member/origin source.
	 * @return The member/origin source.
	 */
	public SourceEntity getSource() {
		return source;
	}
	
	/**
	 * Sets the member/origin source.
	 * @param source The member/origin source.
	 */
	public void setSource(SourceEntity source) {
		this.source = source;
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
		return CommentRateEntity.class;
	}

}
