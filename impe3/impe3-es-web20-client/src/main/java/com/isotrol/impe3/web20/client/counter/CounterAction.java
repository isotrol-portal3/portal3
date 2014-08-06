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
package com.isotrol.impe3.web20.client.counter;


import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

import com.google.common.collect.Sets;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CounterEventDTO;
import com.isotrol.impe3.web20.api.CountersService;
import com.isotrol.impe3.web20.api.SourceDTO;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public class CounterAction {

	/** Counters service. */
	private CountersService service;

	/** Config */
	private CounterConfig moduleConfig;

	@GET
	public Response action(@QueryParam("idr") String idr, @QueryParam("idc") String idc,
		@QueryParam("cgr") List<String> cgr, @QueryParam("ct") String ct, @QueryParam("sm") String sm,
		@QueryParam("so") String so) {

		final CounterEventDTO event = new CounterEventDTO();
		final SourceDTO source = new SourceDTO();
		if (sm != null) {
			source.setMemberId(sm);
		} else {
			source.setOrigin(so);
		}

		if (cgr != null && !cgr.isEmpty()) {
			event.setAggregations(Sets.newHashSet(cgr));
		}
		event.setCommunityId(idc);
		if (ct == null) {
			ct = moduleConfig.counterType();
		}
		event.setCounterType(ct);
		event.setResource(idr);
		event.setSource(source);
		event.setDate(new Date());

		try {
			service.register("", event, true);
		} catch (ServiceException e) {
			return Response.status(500).build();
		}

		//return Response.ok().cacheControl(CacheControl.valueOf("no-store")).build();
		return Response.ok().expires(new Date()).lastModified(new Date()).build();
	}

	public void setService(CountersService service) {
		this.service = service;
	}

	public void setModuleConfig(CounterConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}
}
