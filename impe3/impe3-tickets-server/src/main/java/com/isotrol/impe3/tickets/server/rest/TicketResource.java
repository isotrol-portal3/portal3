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

package com.isotrol.impe3.tickets.server.rest;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.tickets.api.CreatedTicketDTO;
import com.isotrol.impe3.tickets.api.NewTicketDTO;
import com.isotrol.impe3.tickets.domain.Ticket;
import com.isotrol.impe3.tickets.domain.TicketConstraints;
import com.isotrol.impe3.tickets.domain.TicketManager;


/**
 * External resources implementation.
 */
@Path("{subject}")
@Component
public class TicketResource {
	@Autowired
	private TicketManager ticketManager;

	public TicketResource() {
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreatedTicketDTO create(@PathParam("subject") String subject, NewTicketDTO ticket) {
		final TicketConstraints c = TicketConstraints.withDuration(ticket.getDuration(), TimeUnit.SECONDS,
			ticket.getUses());
		final UUID id = ticketManager.create(subject, c, ticket.getPayload());
		return new CreatedTicketDTO(id.toString(), c.getUses(), c.getTimestamp());
	}

	private Response error(int code) {
		return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE)
			.entity(ImmutableMap.of("codError", Integer.toString(code))).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("subject") String subject, @PathParam("id") String id) {
		UUID uuid;
		if (id == null) {
			uuid = null;
		} else {
			try {
				uuid = UUID.fromString(id);
			} catch (IllegalArgumentException e) {
				uuid = null;
			}
		}
		try {
			Ticket t = ticketManager.consume(subject, uuid);
			if (t == null) {
				return error(2);
			}
			switch (t.getState()) {
				case VALID:
					return Response.ok(t.getProperties()).build();
				case CONSUMED:
					return error(3);
				case EXPIRED:
					return error(4);
				default:
					throw new AssertionError();
			}
		} catch (NullPointerException e) {
			return error(1);
		} catch (IllegalArgumentException e) {
			return error(1);
		}
	}
}
