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

package com.isotrol.impe3.tickets.api;


import com.isotrol.impe3.dto.AbstractStringId;
import com.isotrol.impe3.dto.DTOs;


/**
 * DTO representing a created ticket.
 * @author Andres Rodriguez
 */
public class CreatedTicketDTO extends AbstractStringId {
	/** Serial UID. */
	private static final long serialVersionUID = -365553146776804694L;
	/** Number of uses. */
	private int uses;
	/** Ticket expiration timestamp. */
	private long expiration;

	/** Default constructor. */
	public CreatedTicketDTO() {
	}

	/**
	 * Constructor.
	 */
	public CreatedTicketDTO(String id, int uses, long expiration) {
		super(id);
		this.uses = uses;
		this.expiration = expiration;
	}

	/**
	 * Returns the number of uses.
	 * @return The number of uses.
	 */
	public int getUses() {
		return uses;
	}

	/**
	 * Sets the number of uses.
	 * @param uses The number of uses.
	 */
	public void setUses(int uses) {
		this.uses = uses;
	}

	/**
	 * Returns the ticket expiration timestamp.
	 * @return The ticket expiration timestamp.
	 */
	public long getExpiration() {
		return expiration;
	}

	/**
	 * Sets the ticket expiration timestamp.
	 * @param duration The ticket expiration timestamp.
	 */
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	@Override
	public int hashCode() {
		return DTOs.hashCode(getId(), uses, expiration);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof CreatedTicketDTO) {
			final CreatedTicketDTO t = (CreatedTicketDTO) obj;
			return uses == t.uses && expiration == t.expiration && DTOs.equal(getId(), t.getId());
		}
		return false;
	}

}
