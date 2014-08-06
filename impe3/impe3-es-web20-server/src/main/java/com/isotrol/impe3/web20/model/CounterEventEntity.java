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
 * Entity that represents a Counter Entry.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "WEB20_COUNTER_EVENT")
@Immutable
@NamedQuery(name = CounterEventEntity.ALL, query = CounterEventEntity.Q_ALL)
public class CounterEventEntity extends LongIdEntity {
	/** All groups query. */
	public static final String ALL = "counterEvent.all";
	/** Base projection. */
	private static final String FIELDS = "c.counterType.id, c.relationship.group.id, c.relationship.resource.id";
	private static final String PROJ = "select " + FIELDS + " , count(e) as n";
	private static final String FROM = " from CounterEventEntity as e join e.counters as c";
	private static final String W = " where c.relationship.resource.deleted is false";
	private static final String W_DATE = "  and e.timestamp >= ?";
	private static final String GROUP = " group by " + FIELDS;
	/** Date query. */
	static final String Q_ALL = PROJ + FROM + W + W_DATE + GROUP;

	/** Member. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CEVT_SRCE_ID", nullable = false)
	private SourceEntity source;

	/** Timestamp. */
	@Column(name = "CEVT_TIMESTAMP", nullable = false)
	private long timestamp;

	/** Counters. */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "WEB20_EVENT_COUNTER", joinColumns = {@JoinColumn(name = "CEVT_ID")}, inverseJoinColumns = {@JoinColumn(name = "CNTR_ID")})
	private Set<CounterEntity> counters;

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
	 * Returns the counters.
	 * @return The counters.
	 */
	public Set<CounterEntity> getCounters() {
		if (counters == null) {
			counters = Sets.newHashSet();
		}
		return counters;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return CounterEventEntity.class;
	}
}
