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

package com.isotrol.impe3.indexers.client.connector;

import java.net.URI;
import org.springframework.util.StringUtils;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.pms.api.esvc.IndexerDTO;
import com.isotrol.impe3.pms.api.esvc.IndexersDTO;
import com.isotrol.impe3.pms.api.esvc.IndexersService;
import com.isotrol.impe3.tickets.api.jaxrs.GsonReader;
import com.isotrol.impe3.tickets.api.jaxrs.GsonWriter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


	/**
	 * Jersey client-based indexer service implementation.
	 * @author Zineb GHAMIRI
	 */
	public class RESTIndexersService  implements IndexersService{
		private final URI uri;
		private final Client client;
		private final String pathIndexers = "indexers";
		private final String stopIndexerPath= "stop";
		private final String startIndexerPath= "start";
		private final String reindexPath= "reindex";
		private final String reindexAllPath= "reindexAll";

		/**
		 * Constructor.
		 * @param config Configuration.
		 */
		public RESTIndexersService(RESTIndexersServiceConfig config) {
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

		@SuppressWarnings("unchecked")
		public IndexersDTO getIndexers() throws ServiceException {
			if (!StringUtils.hasText(pathIndexers)) {
				throw new ServiceException();
			}
			final WebResource r = resource().path(pathIndexers);
			try {
				return r.get(IndexersDTO.class);
			} catch (RuntimeException e) {
				throw new ServiceException();
			}
		}
		
		@SuppressWarnings("unchecked")
		public IndexerDTO stopIndexer(String name,String id) throws ServiceException {
			if (!StringUtils.hasText(stopIndexerPath)) {
				throw new ServiceException();
			}
			String stopIndexerPath= "stop"+"/"+name;
			final WebResource r = resource().path(stopIndexerPath);
			try {
				return r.get(IndexerDTO.class);
			} catch (RuntimeException e) {
				throw new ServiceException();
			}
		}
		
		@SuppressWarnings("unchecked")
		public IndexerDTO startIndexer(String name,String id) throws ServiceException {
			if (!StringUtils.hasText(startIndexerPath)) {
				throw new ServiceException();
			}
			String startIndexerPath= "start"+"/"+name;
			final WebResource r = resource().path(startIndexerPath);
			try {
				return r.get(IndexerDTO.class);
			} catch (RuntimeException e) {
				throw new ServiceException();
			}
		}
		
		@SuppressWarnings("unchecked")
		public IndexerDTO reindex(String name,String id) throws ServiceException {
			if (!StringUtils.hasText(reindexPath)) {
				throw new ServiceException();
			}
			String reindexPath= "reindex"+"/"+name;
			final WebResource r = resource().path(reindexPath);
			try {
				return r.get(IndexerDTO.class);
			} catch (RuntimeException e) {
				throw new ServiceException();
			}
		}
		
		@SuppressWarnings("unchecked")
		public IndexerDTO reindexAll(String name,String id, boolean copia) throws ServiceException {
			if (!StringUtils.hasText(reindexAllPath)) {
				throw new ServiceException();
			}
			String reindexAllPath= "reindexAll"+"/"+name+"/"+copia;
			final WebResource r = resource().path(reindexAllPath);
			try {
				return r.get(IndexerDTO.class);
			} catch (RuntimeException e) {
				throw new ServiceException();
			}
		}
	}



