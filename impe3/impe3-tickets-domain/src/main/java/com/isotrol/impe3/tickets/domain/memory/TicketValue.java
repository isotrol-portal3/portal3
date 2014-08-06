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

package com.isotrol.impe3.tickets.domain.memory;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.isotrol.impe3.tickets.domain.Ticket;
import com.isotrol.impe3.tickets.domain.TicketConstraints;
import com.isotrol.impe3.tickets.domain.TicketState;


/**
 * Ticket value for the map-backed implementation.
 * @author Andres Rodriguez
 */
final class TicketValue {
	/** Initial ticket. */
	private final Ticket ticket;
	/** Ticket constraints. */
	private final TicketConstraints constraints;
	/** Current ticket. */
	private volatile Ticket current;
	/** Available uses. */
	private volatile int available;

	TicketValue(Ticket ticket, TicketConstraints constraints) {
		this.ticket = checkNotNull(ticket);
		this.constraints = checkNotNull(constraints);
		this.current = ticket;
		this.available = constraints.getUses();
	}

	/** Returns the ticket id. */
	public UUID getId() {
		return ticket.getId();
	}

	Ticket consume() {
		if (current.getState() != TicketState.VALID) {
			return current;
		}
		return doConsume();
	}

	private synchronized Ticket doConsume() {
		if (current.getState() != TicketState.VALID) {
			return current;
		}
		if (available <= 0) {
			current = Ticket.consumed(getId());
		} else {
			available--;
			if (constraints.isExpired()) {
				current = Ticket.expired(getId());
			}
		}
		return current;
	}

}
