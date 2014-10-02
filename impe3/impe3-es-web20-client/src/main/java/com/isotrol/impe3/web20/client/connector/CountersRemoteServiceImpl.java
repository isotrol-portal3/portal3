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
package com.isotrol.impe3.web20.client.connector;


import java.util.List;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CounterEventDTO;
import com.isotrol.impe3.web20.api.CounterFilterDTO;
import com.isotrol.impe3.web20.api.CountersService;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public class CountersRemoteServiceImpl extends WithHessian<CountersService> implements CountersService {

	@Override
	protected Class<CountersService> serviceClass() {
		return CountersService.class;
	}

	@Override
	protected String serviceUrl() {
		return server() + "/counters";
	}

	public void register(String serviceId, CounterEventDTO event, boolean async) throws ServiceException {
		delegate().register(serviceId, event, async);
	}

	public List<ResourceCounterDTO> getGreatestHits(String serviceId, CounterFilterDTO filter, int max)
		throws ServiceException {
		return delegate().getGreatestHits(serviceId, filter, max);
	}

	public long getResourceHits(String serviceId, CounterFilterDTO filter, String resource) throws ServiceException {
		return delegate().getResourceHits(serviceId, filter, resource);
	}

}
