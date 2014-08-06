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


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.isotrol.impe3.es.common.model.LongIdEntity;


/**
 * Entity that represents a group.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "WEB20_GROUP")
@NamedQueries( {
	@NamedQuery(name = GroupEntity.WOA, query = "from GroupEntity as e where community.id = ? and aggregation is null"),
	@NamedQuery(name = GroupEntity.WA, query = "from GroupEntity as e where community.id = ? and aggregation.id = ?")})
public class GroupEntity extends LongIdEntity {
	/** Query with no aggregation. */
	public static final String WOA = "group.withoutAgg";
	/** Query with aggregation. */
	public static final String WA = "group.withAgg";

	/** Community. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRUP_CMTY_ID", nullable = false)
	private CommunityEntity community;

	/** Aggregation, optional. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRUP_GGRT_ID", nullable = true)
	private AggregationEntity aggregation;

	/** Constructor. */
	public GroupEntity() {
	}

	/**
	 * Returns the community.
	 * @return The community.
	 */
	public CommunityEntity getCommunity() {
		return community;
	}

	/**
	 * Sets the commnunity
	 * @param comunity The community.
	 */
	public void setCommunity(CommunityEntity comunity) {
		this.community = comunity;
	}

	/**
	 * Returns the counter aggregation.
	 * @return The counter aggregation.
	 */
	public AggregationEntity getAggregation() {
		return aggregation;
	}

	/**
	 * Sets the counter aggregation.
	 * @param aggregation The counter aggregation.
	 */
	public void setAggregation(AggregationEntity aggregation) {
		this.aggregation = aggregation;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return GroupEntity.class;
	}

}
