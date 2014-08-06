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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.es.common.model.VersionedLongIdEntity;


/**
 * Entity that represents a Resource Rating.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "WEB20_RATING")
@NamedQueries({@NamedQuery(name = RatingEntity.REL, query = "from RatingEntity as e where relationship.id = ?"),
	@NamedQuery(name = RatingEntity.ALL, query = RatingEntity.Q_ALL)})
public class RatingEntity extends VersionedLongIdEntity {
	/** Query by relationship. */
	public static final String REL = "rating.rel";
	/** All groups query. */
	public static final String ALL = "rating.all";

	/** Group by fields. */
	static final String FIELDS = "r.relationship.group.id, r.relationship.resource.id";
	/** Proyection. */
	static final String PROJ = "select " + FIELDS + " , r.version, r.min, r.max, r.mean";
	/** From clause. */
	static final String FROM = " from RatingEntity as r";
	/** Basic where. */
	static final String W = " where r.relationship.resource.deleted is false";
	/** All groups query. */
	static final String Q_ALL = PROJ + FROM + W;

	/** Relationship. */
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "RTNG_RLTS_ID", nullable = false)
	private RelationshipEntity relationship;
	/** Minimum value. */
	@Column(name = "RTNG_MIN", nullable = false)
	private int min = 0;
	/** Maximum value. */
	@Column(name = "RTNG_MAX", nullable = false)
	private int max = 0;
	/** Arithmetic mean. */
	@Column(name = "RTNG_AVG", nullable = false)
	private double mean = 0;

	/** Default constructor. */
	public RatingEntity() {
	}

	/**
	 * Returns the relationship.
	 * @return The relationship.
	 */
	public RelationshipEntity getRelationship() {
		return relationship;
	}

	/**
	 * Sets the relationship.
	 * @param relationship The relationship.
	 */
	public void setRelationship(RelationshipEntity relationship) {
		this.relationship = relationship;
	}

	/**
	 * Returns the number of ratings.
	 * @return The number of ratings.
	 */
	public int getCount() {
		return getVersion();
	}

	/**
	 * Returns the minimum value.
	 * @return The minimum value.
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Returns the maximum value.
	 * @return The maximum value.
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Returns the arithmetic mean.
	 * @return The arithmetic mean.
	 */
	public double getMean() {
		return mean;
	}

	public void add(int rating) {
		final int i = getCount() + 1;
		if (i == 1) {
			min = rating;
			max = rating;
			mean = rating;
		} else {
			min = Math.min(min, rating);
			max = Math.max(max, rating);
			mean = mean + (rating - mean) / i;
		}
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return RatingEntity.class;
	}

}
