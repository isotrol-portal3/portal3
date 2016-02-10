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

package com.isotrol.impe3.indexers.server;




import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.pms.api.esvc.IndexerDTO;
import com.isotrol.impe3.pms.api.esvc.IndexersDTO;




@Path("idx")
@Component
public class IndexerResource {
	@Autowired
	private IndexersManager indexerManager;

	public IndexerResource() {
	}

	@GET
	@Path("indexers")
	@Produces(MediaType.APPLICATION_JSON)
	public IndexersDTO get() {
		return indexerManager.getIndexers();
	}
	
	
	@GET
	@Path("stop/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IndexerDTO stop(@PathParam("name") String name) {
		return indexerManager.stop(name);
	}
	
	@GET
	@Path("start/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IndexerDTO start(@PathParam("name") String name) {
		return indexerManager.start(name);
	}
	
	@GET
	@Path("reindex/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IndexerDTO reindex(@PathParam("name") String name) {
		return indexerManager.reindex(name);
	}
	
	@GET
	@Path("reindexAll/{name}/{copia}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IndexerDTO reindexAll(@PathParam("name") String name,@PathParam(value = "copia") boolean copia) {
		return indexerManager.reindexAll(name,copia);
	}
}
