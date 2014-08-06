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


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.google.common.collect.Sets;
import com.isotrol.impe3.es.common.model.LongIdEntity;


/**
 * Entity that represents a rating event.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "WEB20_RATING_EVENT")
@Immutable
@NamedQuery(name = RatingEventEntity.ALL, query = RatingEventEntity.Q_ALL)
public class RatingEventEntity extends LongIdEntity {
	/** All groups query. */
	public static final String ALL = "ratingEvent.all";

	/** Group by fields. */
	private static final String FIELDS = "r.relationship.group.id, r.relationship.resource.id";
	/** Proyection. */
	private static final String PROJ = "select " + FIELDS + " , count(e.id), min(e.value), max(e.value), avg(e.value)";
	/** From clause. */
	private static final String FROM = " from RatingEventEntity e join e.ratings as r";
	/** Where. */
	private static final String W = " where e.value is not null and r.relationship.resource.deleted is false and e.timestamp >= ?";
	/** All groups query. */
	static final String Q_ALL = PROJ + FROM + W + " group by " + FIELDS;

	/** Member. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "REVT_SRCE_ID", nullable = false)
	private SourceEntity source;

	/** Timestamp. */
	@Column(name = "REVT_TIMESTAMP", nullable = false)
	private long timestamp;

	/** Rating value. */
	@Column(name = "REVT_VALUE", nullable = true)
	private Integer value;

	/** Counters. */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "WEB20_EVENT_RATING", joinColumns = {@JoinColumn(name = "REVT_ID")}, inverseJoinColumns = {@JoinColumn(name = "RTNG_ID")})
	private Set<RatingEntity> ratings;

	/**
	 * Returns the source.
	 * @return The source.
	 */
	public SourceEntity getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 * @param source The source.
	 */
	public void setSource(SourceEntity source) {
		this.source = source;
	}

	/**
	 * Returns the timestamp.
	 * @return The timestamp.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 * @param timestamp The timestamp.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Returns the rating value.
	 * @return The rating value.
	 */
	public int getValue() {
		return value != null ? value.intValue() : 0;
	}

	/**
	 * Sets the rating value.
	 * @param value The rating value.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Returns the ratings.
	 * @return The ratings.
	 */
	public Set<RatingEntity> getRatings() {
		if (ratings == null) {
			ratings = Sets.newHashSet();
		}
		return ratings;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return RatingEventEntity.class;
	}

}
