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
import java.util.Map;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.EventDTO;
import com.isotrol.impe3.web20.api.EventFilterDTO;
import com.isotrol.impe3.web20.api.RatingEventDTO;
import com.isotrol.impe3.web20.api.RatingsService;
import com.isotrol.impe3.web20.api.ResourceRatingDTO;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public class RatingsRemoteServiceImpl extends WithHessian<RatingsService> implements RatingsService {

	@Override
	protected Class<?> serviceClass() {
		return RatingsService.class;
	}

	@Override
	protected String serviceUrl() {
		return server() + "/ratings";
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#isRated(java.lang.String, com.isotrol.impe3.web20.api.EventDTO)
	 */
	public boolean isRated(String serviceId, EventDTO event) throws ServiceException {
		return delegate().isRated(serviceId, event);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#getBestRated(java.lang.String,
	 * com.isotrol.impe3.web20.api.EventFilterDTO, int)
	 */
	public List<ResourceRatingDTO> getBestRated(String serviceId, EventFilterDTO filter, int max)
		throws ServiceException {
		return delegate().getBestRated(serviceId, filter, max);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#getResourceRatings(java.lang.String,
	 * com.isotrol.impe3.web20.api.EventFilterDTO, java.lang.String)
	 */
	public ResourceRatingDTO getResourceRatings(String serviceId, EventFilterDTO filter, String resource)
		throws ServiceException {
		return delegate().getResourceRatings(serviceId, filter, resource);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#register(java.lang.String,
	 * com.isotrol.impe3.web20.api.RatingEventDTO, boolean, boolean)
	 */
	public void register(String serviceId, RatingEventDTO event, boolean async, boolean allowMore)
		throws ServiceException {
		delegate().register(serviceId, event, async, allowMore);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#getRatingsOfResources(java.lang.String, java.util.List,
	 * com.isotrol.impe3.web20.api.EventFilterDTO)
	 */
	public Map<String, ResourceRatingDTO> getRatingsOfResources(String serviceId, List<String> resources,
		EventFilterDTO filter) throws ServiceException {
		return delegate().getRatingsOfResources(serviceId, resources, filter);
	}
}
