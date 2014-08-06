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
 * Entity that represents a Counter.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "WEB20_COUNTER")
@NamedQueries({
	@NamedQuery(name = CounterEntity.TR, query = "from CounterEntity as e where counterType.id = ? and relationship.id = ?"),
	@NamedQuery(name = CounterEntity.INC, query = "update CounterEntity set total = total+1 where id = ?"),
	@NamedQuery(name = CounterEntity.ALL, query = CounterEntity.Q_ALL)})
public class CounterEntity extends LongIdEntity {
	/** Query by group and resource. */
	public static final String TR = "counter.gr";
	/** All groups query. */
	public static final String ALL = "counter.all";
	/** Increment query. */
	public static final String INC = "counter.inc";

	/** Group by fields. */
	static final String FIELDS = "c.counterType.id, c.relationship.group.id, c.relationship.resource.id";
	/** Proyection. */
	static final String PROJ = "select " + FIELDS + " , c.total";
	/** From clause. */
	static final String FROM = " from CounterEntity as c";
	/** Basic where. */
	static final String W = " where c.relationship.resource.deleted is false";
	/** Global group. */
	static final String W_GLOBAL = " and c.relationship.group is null";
	/** Specific group. */
	static final String W_GROUP = " and c.relationship.group.id = ?";
	/** All groups query. */
	static final String Q_ALL = PROJ + FROM + W;

	/** Counter group. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CNTR_CNTY_ID", nullable = false)
	private CounterTypeEntity counterType;

	/** Relationship. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CNTR_RLTS_ID", nullable = false)
	private RelationshipEntity relationship;

	/** Total count. */
	@Column(name = "CNTR_TOTAL", nullable = false)
	private long total = 0;

	/** Constructor. */
	public CounterEntity() {
	}

	/**
	 * Returns the counter group.
	 * @return The counter group.
	 */
	public CounterTypeEntity getCounterType() {
		return counterType;
	}

	/**
	 * Sets the counter type.
	 * @param counterType The counter type.
	 */
	public void setCounterType(CounterTypeEntity counterType) {
		this.counterType = counterType;
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
	 * Returns the total number of hits.
	 * @return The total number of hits.
	 */
	public long getTotal() {
		return total;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return CounterEntity.class;
	}

}
