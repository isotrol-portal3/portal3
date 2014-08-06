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

package com.isotrol.impe3.tickets.domain;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;


/**
 * Domain object for a ticket.
 * @author Andres Rodriguez
 */
public abstract class Ticket {
	public static Ticket valid(UUID id, Map<String, String> properties) {
		return new Valid(id, properties);
	}

	public static Ticket expired(UUID id) {
		return new Expired(id);
	}

	public static Ticket consumed(UUID id) {
		return new Consumed(id);
	}

	/** Ticket id. */
	private final UUID id;

	/**
	 * Constructor.
	 * @param id Id.
	 */
	private Ticket(UUID id) {
		this.id = checkNotNull(id);
	}

	/** Returns the ticket id. */
	public UUID getId() {
		return id;
	}

	/** Returns the ticket state. */
	public abstract TicketState getState();

	/**
	 * Returns the ticket properties.
	 * @return The ticket properties.
	 * @throws IllegalStateException if the ticket is not found.
	 */
	public ImmutableMap<String, String> getProperties() {
		throw new IllegalStateException();
	}

	private static final class Valid extends Ticket {
		private final ImmutableMap<String, String> properties;

		Valid(UUID id, Map<String, String> properties) {
			super(id);
			if (properties == null) {
				this.properties = ImmutableMap.of();
			} else {
				this.properties = ImmutableMap.copyOf(properties);
			}
		}

		@Override
		public TicketState getState() {
			return TicketState.VALID;
		}

		@Override
		public ImmutableMap<String, String> getProperties() {
			return properties;
		}
	}

	private static final class Expired extends Ticket {
		Expired(UUID id) {
			super(id);
		}

		@Override
		public TicketState getState() {
			return TicketState.EXPIRED;
		}
	}

	private static final class Consumed extends Ticket {
		Consumed(UUID id) {
			super(id);
		}

		@Override
		public TicketState getState() {
			return TicketState.CONSUMED;
		}
	}
}
