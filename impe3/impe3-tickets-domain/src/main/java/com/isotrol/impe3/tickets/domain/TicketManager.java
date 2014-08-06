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


import java.util.Map;
import java.util.UUID;


/**
 * Interface for the ticket manager.
 * @author Andres Rodriguez
 */
public interface TicketManager {
	/**
	 * Creates a new ticket.
	 * @param subject Ticket subject.
	 * @param constraints Constraints.
	 * @return The created ticket id.
	 * @throws NullPointerException if the subject or the constraints are null.
	 */
	UUID create(String subject, TicketConstraints constraints, Map<String, String> properties);

	/**
	 * Consumes a ticket.
	 * @param subject Ticket subject.
	 * @param id Ticket id.
	 * @return The requested ticket or {@code null} if the ticket is {@code null} or is not found.
	 * @throws NullPointerException if the subject is {@code null}
	 * @throws IllegalArgumentException if the subject is not found.
	 */
	Ticket consume(String subject, UUID id);
}
