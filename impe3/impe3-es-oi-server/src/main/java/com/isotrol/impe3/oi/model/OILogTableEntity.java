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
package com.isotrol.impe3.oi.model;


import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.isotrol.impe3.hib.model.Lengths;


/**
 * Entity that represents a Log table entry.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "OI_LOG")
@NamedQueries( {@NamedQuery(name = OILogTableEntity.BY_NAME, query = "from OILogTableEntity as e where e.name = ? and e.id > ?")})
public class OILogTableEntity {

	/** Query by name. */
	public static final String BY_NAME = "oiLogTable.byName";

	/** id. */
	@Id
	@Column(name = "ID", nullable = false)
	private Long id;

	/** Item id. */
	@Type(type = "impeId")
	@Column(name = "LOG_ITEM", length = Lengths.UUID, nullable = false)
	private UUID item;

	/** Item type. */
	@Column(name = "LOG_NAME", length = 4, nullable = false)
	private String name;

	/** Task name. */
	@Column(name = "LOG_TASK", nullable = false)
	private int task;

	/** Creation date. */
	@Column(name = "LOG_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "LOG_REPLACED_ID", nullable = true)
	private OILogTableEntity replacedBy;

	/** Constructor. */
	public OILogTableEntity() {
	}

	/**
	 * Returns the id.
	 * @return The id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id The id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns the item id.
	 * @return The item id.
	 */
	public UUID getItem() {
		return item;
	}

	/**
	 * Sets the item id.
	 * @param item The item id.
	 */
	public void setItem(UUID item) {
		this.item = item;
	}

	/**
	 * Returns the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the task.
	 * @return The task.
	 */
	public int getTask() {
		return task;
	}

	/**
	 * Sets the task.
	 * @param task The task.
	 */
	public void setTask(int task) {
		this.task = task;
	}

	/**
	 * Returns the insert date.
	 * @return The insert date.
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Sets the insert date.
	 * @param date The insert date.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Returns the replaced by reference.
	 * @return The replaced by reference.
	 */
	public OILogTableEntity getReplacedBy() {
		return replacedBy;
	}

	/**
	 * Sets the replaced by reference.
	 * @param replacedBy The replaced by reference.
	 */
	public void setReplacedBy(OILogTableEntity replacedBy) {
		this.replacedBy = replacedBy;
	}
}
