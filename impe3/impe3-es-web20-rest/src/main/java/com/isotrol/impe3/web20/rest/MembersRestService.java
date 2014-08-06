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
package com.isotrol.impe3.web20.rest;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MemberSelDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;
import com.sun.jersey.spi.resource.Singleton;


@Singleton
@Path("/members/")
@Produces("application/json")
@Consumes("application/json")
/**
 * Members Jersey REST service implementation.
 * @author Emilio Escobar Reyero
 */
public class MembersRestService {

	@Autowired
	private MembersService service;
	private String serviceId;

	@GET
	@Path("search")
	public PageDTO<MemberSelDTO> search(PageFilter<MemberFilterDTO> filter) throws ServiceException {
		// TODO recover parameters
		return service.search(serviceId, filter);
	}

	@GET
	@Path("id/{id}")
	public MemberDTO getById(@PathParam("id") String id) throws ServiceException {
		return service.getById(serviceId, id);
	}

	@GET
	@Path("login/{login}")
	@Produces("application/json")
	public MemberDTO getByCode(@PathParam("login") String login) throws ServiceException {
		return service.getByCode(serviceId, login);
	}

	@GET
	@Path("membership/{id}")
	public PageDTO<MembershipSelDTO> getMembership(@PathParam("id") String id, @QueryParam("validated") Boolean validated) throws ServiceException {
		final PageFilter<MembershipSelFilterDTO> filter = new PageFilter<MembershipSelFilterDTO>();
		final MembershipSelFilterDTO mfilter = new MembershipSelFilterDTO();
		mfilter.setId(id);
		mfilter.setValidated(validated);
		filter.setFilter(mfilter);
		
		return service.getMemberships(id, filter);
	}

	@POST
	@Path("create")
	public void create(MemberDTO member) throws ServiceException {
		service.create(serviceId, member);
	}

	@PUT
	@Path("id/{id}")
	public void updateById(@PathParam("id") String id, MemberDTO member) throws ServiceException {
		// TODO check id and member.getId
		service.update(serviceId, member);
	}

	@PUT
	@Path("login/{login}")
	public void updateByCode(@PathParam("login") String id, MemberDTO member) throws ServiceException {
		// TODO check code and member.getCode
		service.update(serviceId, member);
	}

	@DELETE
	@Path("id/{id}")
	public void deleteById(@PathParam("id") String id) throws ServiceException {
		service.delete(serviceId, id);
	}

	@DELETE
	@Path("login/{login}")
	public void deleteByCode(@PathParam("login") String login) throws ServiceException {
		final MemberDTO member = service.getByCode(serviceId, login);
		service.delete(serviceId, member.getId());
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setService(MembersService service) {
		this.service = service;
	}

}
