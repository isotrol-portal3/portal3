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
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.Preconditions;

/**
 * Entity representing database row-backed sequences. The use of these kind of
 * sequences is the possibility of using them transactionally. WARNING:
 * Transactional sequences are a source of locks and may hurt scalability.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "WEB20_SEQUENCE")
public class SequenceEntity {
	/** Name of the sequence (identifier). */
	@Id
	@Column(name = "TXSQ_NAME", nullable = false)
	private String id;
	/** Current value of the sequence. */
	@Column(name = "TXSQ_CURRENT", nullable = false)
	private long current;


	/** Default constructor. */
	public SequenceEntity() {
	}

	/**
	 * Initializes a new sequence.
	 * @param id Sequence name.
	 * @param initial Initial value.
	 */
	public SequenceEntity(final String id, final long initial) {
		this.id = Preconditions.checkNotNull(id, "Sequence name is mandatory.");
		this.current = initial;
	}

	/**
	 * Returns the name of the sequence.
	 * @return The name of the sequence.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the current value of the sequence.
	 * @return The current value of the sequence.
	 */
	public long getCurrent() {
		return current;
	}

	/**
	 * Increments the sequence.
	 * @return The new value of the sequence.
	 */
	public long getNext() {
		current += 1;
		return current;
	}
	
}
