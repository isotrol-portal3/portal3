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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.es.common.model.VersionedLongIdEntity;
import com.isotrol.impe3.hib.model.Lengths;


/**
 * Entity that represents a Comment.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_COMMENT")
@NamedQueries({
	@NamedQuery(name = CommentEntity.NOT_DELETED, query = "from CommentEntity as e where e.deleted is false"),
	@NamedQuery(name = CommentEntity.NOT_MODERATED, query = "from CommentEntity as e where e.lastModeration is null and e.deleted is false"),
	@NamedQuery(name = CommentEntity.ALL, query = "select e.relationship.resource.resourceId, e.relationship.group.community.id, e.valid, e.lastModeration from CommentEntity as e where e.deleted is false and e.relationship.resource.deleted is false"),
	@NamedQuery(name = CommentEntity.ALL_TIMED, query = "select e.relationship.resource.resourceId, e.relationship.group.community.id, e.valid, e.lastModeration from CommentEntity as e where e.deleted is false and e.relationship.resource.deleted is false and e.lastUpdated > ?"),
	@NamedQuery(name = CommentEntity.VALID, query = "from CommentEntity as e where e.valid is true and e.deleted is false"),
	@NamedQuery(name = CommentEntity.RWUC, query = "select e.relationship.resource.resourceId, e.relationship.group.community, count(e.id) from CommentEntity as e where e.lastModeration is null and e.deleted is false group by e.relationship.resource.resourceId, e.relationship.group.community"),
	@NamedQuery(name = CommentEntity.RWUC_IC, query = "select e.relationship.resource.resourceId, count(e.id) from CommentEntity as e where e.relationship.group.community.id = ? and e.lastModeration is null and e.deleted is false group by e.relationship.resource.resourceId")})
public class CommentEntity extends VersionedLongIdEntity {
	/** Query by not deleted. */
	public static final String NOT_DELETED = "comment.notDeleted";
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

	/** Comment title. */
	@Column(name = "CMMT_TITLE", length = Lengths.TITLE, unique = false, nullable = false)
	private String title;
	/** Comment description. */
	@Column(name = "CMMT_DESCRIPTION", length = Lengths.DESCRIPTION, unique = false, nullable = false)
	private String description;

	/** Comment email. */
	@Column(name = "CMMT_EMAIL", length = Lengths.TITLE, unique = false, nullable = false)
	private String email;

	/** Valid comment. */
	@Column(name = "CMMT_VALID", nullable = false)
	private Boolean valid;

	/** Actual rate value. */
	@Column(name = "CMMT_RATE", nullable = false)
	private Double rate;

	/** Actual rate value. */
	@Column(name = "CMMT_RATES", nullable = false)
	private Integer numberOfRates;

	/** Timestamp. */
	@Column(name = "CMMT_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	/** Timestamp. */
	@Column(name = "CMMT_LAST", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar lastModeration;

	/** Timestamp. */
	@Column(name = "CMMT_UPDATED", nullable = true)
	private Long lastUpdated = Calendar.getInstance().getTimeInMillis();

	/** Whether the comment is deleted. */
	@Column(name = "CMMT_DELETED", nullable = false)
	private boolean deleted = false;

	/** Relationship. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CMMT_RLTS_ID", nullable = false)
	private RelationshipEntity relationship;

	/** Source Member. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CMMT_SRCE_ID", nullable = false)
	private SourceEntity source;

	/** Default constructor. */
	public CommentEntity() {
	}

	/**
	 * Returns the comment title.
	 * @return The comment title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the comment title.
	 * @param title The comment title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the comment description.
	 * @return The comment description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the comment description.
	 * @param description The comment description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the email.
	 * @return The email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 * @param email The email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns whether the comment is valid.
	 * @return True if the comment is valid.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Sets whether the comment is valid.
	 * @param deleted True if the comment is valid.
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * Returns the comment rate.
	 * @return The comment rate.
	 */
	public double getRate() {
		return rate == null ? 0 : rate;
	}

	/**
	 * Sets the comment rate.
	 * @param rate The comment rate.
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * Returns the comment commit date.
	 * @return The comment commit date.
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Sets the comment total rates.
	 * @param rates The number of rates.
	 */
	public void setNumberOfRates(Integer numberOfRates) {
		this.numberOfRates = numberOfRates;
	}

	/**
	 * Returns the number of rates.
	 * @return The number of rates.
	 */
	public Integer getNumberOfRates() {
		return numberOfRates == null ? 0 : numberOfRates;
	}

	/**
	 * Sets the comment commit date.
	 * @param date The comment commit date.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Returns whether the comment is deleted.
	 * @return True if the comment is deleted.
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets whether the comment is deleted.
	 * @param deleted True if the comment is deleted.
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
		this.lastUpdated = Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * Returns the last moderation date.
	 * @return The last moderation date.
	 */
	public Calendar getLastModeration() {
		return lastModeration;
	}

	/**
	 * Sets the last moderation date.
	 * @param lastModeration The last moderation date.
	 */
	public void setLastModeration(Calendar lastModeration) {
		this.lastModeration = lastModeration;
		this.lastUpdated = lastModeration.getTimeInMillis();
	}

	/**
	 * Returns the last update timestamp.
	 * @return The last update timestamp.
	 */
	public long getLastUpdated() {
		if (lastUpdated == null) {
			return lastUpdated;
		}
		return Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * Returns the comment relationship.
	 * @return The comment relationship.
	 */
	public RelationshipEntity getRelationship() {
		return relationship;
	}

	/**
	 * Sets the comment relationship
	 * @param relationship The comment relationship.
	 */
	public void setRelationship(RelationshipEntity relationship) {
		this.relationship = relationship;
	}

	/**
	 * Returns the comment source.
	 * @return The comment source.
	 */
	public SourceEntity getSource() {
		return source;
	}

	/**
	 * Sets the comment source;
	 * @param source The comment source;
	 */
	public void setSource(SourceEntity source) {
		this.source = source;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return CommentEntity.class;
	}
}
