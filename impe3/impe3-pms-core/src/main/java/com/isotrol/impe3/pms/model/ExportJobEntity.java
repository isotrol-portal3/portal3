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


import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import com.google.common.collect.Sets;


/**
 * Entity that represents an export job.
 * @author Andres Rodriguez
 */
@Entity
@Immutable
@Table(name = "PMS_EXPORT_JOB")
public class ExportJobEntity extends WithCreatedEntity {
	/** Job type. */
	@Column(name = "EJOB_TYPE", length = Lengths.NAME, nullable = false)
	@Enumerated(EnumType.STRING)
	private ExportJobType type;
	/** Main object. */
	@Column(name = "EJOB_MAIN_ID", length = Lengths.UUID, nullable = true)
	@Type(type = "impeId")
	private UUID mainId;
	/** Secondary object. */
	@Column(name = "EJOB_OTHER_ID", length = Lengths.UUID, nullable = true)
	@Type(type = "impeId")
	private UUID otherId;
	/** Other ids. */
	@CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(name = "PMS_EXPORT_JOB_ITEM", joinColumns = @JoinColumn(name = "EJOB_ID", nullable = false))
	@Column(name = "EJOB_ITEM_ID")
	@Type(type = "impeId")
	private Set<UUID> objects;

	/** Default constructor. */
	public ExportJobEntity() {
	}

	/**
	 * Returns the job type.
	 * @return The job type.
	 */
	public ExportJobType getType() {
		return type;
	}

	/**
	 * Sets the job type.
	 * @param type The job type.
	 */
	public void setType(ExportJobType type) {
		this.type = type;
	}

	/**
	 * Returns the main object id.
	 * @return The main object id.
	 */
	public UUID getMainId() {
		return mainId;
	}
	
	/**
	 * Returns the secondary id.
	 * @return The secondary id.
	 */
	public UUID getOtherId() {
		return otherId;
	}

	/**
	 * Sets the main object id.
	 * @param mainId The main object id.
	 */
	public void setMainId(UUID mainId) {
		this.mainId = mainId;
	}
	
	/**
	 * Sets the secondary id.
	 * @param otherId The secondary id.
	 */
	public void setOtherId(UUID otherId) {
		this.otherId = otherId;
	}

	/**
	 * Returns the set of objects.
	 * @return The set of objects.
	 */
	public Set<UUID> getObjects() {
		if (objects == null) {
			objects = Sets.newHashSet();
		}
		return objects;
	}
}
