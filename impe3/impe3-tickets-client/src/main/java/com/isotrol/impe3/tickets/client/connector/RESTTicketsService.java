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
package com.isotrol.impe3.tickets.client.connector;


import java.net.URI;

import org.springframework.util.StringUtils;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.tickets.api.CreatedTicketDTO;
import com.isotrol.impe3.tickets.api.NewTicketDTO;
import com.isotrol.impe3.tickets.api.TicketsService;
import com.isotrol.impe3.tickets.api.jaxrs.GsonReader;
import com.isotrol.impe3.tickets.api.jaxrs.GsonWriter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


/**
 * Jersey client-based tickets service implementation.
 * @author Andres Rodriguez
 */
public class RESTTicketsService implements TicketsService {
	private final URI uri;
	private final Client client;

	/**
	 * Constructor.
	 * @param config Configuration.
	 */
	public RESTTicketsService(RESTTicketsServiceConfig config) {
		URI u = null;
		try {
			u = new URI(config.serviceBaseURI());
		} catch (Exception e) {}
		this.uri = u;
		if (u != null) {
			final ClientConfig cc = new DefaultClientConfig();
			cc.getClasses().add(GsonWriter.class);
			cc.getClasses().add(GsonReader.class);
			this.client = Client.create(cc);
		} else {
			client = null;
		}
	}

	private WebResource resource() throws ServiceException {
		if (uri == null || client == null) {
			throw new ServiceException();
		}
		return client.resource(uri);
	}

	public CreatedTicketDTO create(String serviceId, String subject, NewTicketDTO ticket) throws ServiceException {
		if (!StringUtils.hasText(subject) || ticket == null) {
			throw new ServiceException();
		}
		final WebResource r = resource().path(subject);
		try {
			return r.post(CreatedTicketDTO.class, ticket);
		} catch (RuntimeException e) {
			throw new ServiceException();
		}
	}
}
