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


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.isotrol.impe3.dto.DTOs;


/**
 * DTO representing a new ticket.
 * @author Andres Rodriguez
 */
public class NewTicketDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -2486087393949051517L;
	/** Number of uses. */
	private int uses;
	/** Ticket duration in seconds. */
	private int duration;
	/** Ticket payload. */
	private Map<String, String> payload;

	/** Default constructor. */
	public NewTicketDTO() {
	}

	/**
	 * Constructor.
	 */
	public NewTicketDTO(int uses, int duration, Map<String, String> payload) {
		this.uses = uses;
		this.duration = duration;
		if (payload != null) {
			this.payload = payload;
		} else {
			this.payload = new HashMap<String, String>();
		}
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
	 * Returns the ticket duration in seconds.
	 * @return The ticket duration in seconds.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the ticket duration in seconds.
	 * @param duration The ticket duration in seconds.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Returns the ticket payload.
	 * @return The ticket payload.
	 */
	public Map<String, String> getPayload() {
		return payload;
	}

	/**
	 * Sets the ticket payload.
	 * @param payload The payload.
	 */
	public void setPayload(Map<String, String> payload) {
		this.payload = payload;
	}

	@Override
	public int hashCode() {
		return DTOs.hashCode(uses, duration, payload);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof NewTicketDTO) {
			final NewTicketDTO t = (NewTicketDTO) obj;
			return uses == t.uses && duration == t.duration && DTOs.equal(payload, t.payload);
		}
		return false;
	}

}
