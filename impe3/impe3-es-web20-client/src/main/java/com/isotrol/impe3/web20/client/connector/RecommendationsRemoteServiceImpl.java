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


import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilterDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.RecommendationDTO;
import com.isotrol.impe3.web20.api.RecommendationFilterDTO;
import com.isotrol.impe3.web20.api.RecommendationsService;


/**
 * Recommendation remote service implementation.
 * @author Emilio Escobar Reyero
 */
public class RecommendationsRemoteServiceImpl extends WithHessian<RecommendationsService> implements
	RecommendationsService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class serviceClass() {
		return RecommendationsService.class;
	}

	@Override
	protected String serviceUrl() {
		return server() + "/recommendations";
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RecommendationsService#getRecommendedResources(java.lang.String, java.lang.String, java.lang.String)
	 */
	public PageDTO<RecommendationDTO> getRecommendedResources(String serviceId, String member, String community, PageFilterDTO pagination)
		throws ServiceException {
		return delegate().getRecommendedResources(serviceId, member, community, pagination);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RecommendationsService#recommendResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Long recommendResource(String serviceId, RecommendationDTO recommendation) throws ServiceException {
		return delegate().recommendResource(serviceId, recommendation);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RecommendationsService#removeRecommendation(java.lang.String, java.lang.Long)
	 */
	public void removeRecommendation(String serviceId, Long recommendation) throws ServiceException {
		delegate().removeRecommendation(serviceId, recommendation);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RecommendationsService#search(java.lang.String, com.isotrol.impe3.web20.api.RecommendationFilterDTO)
	 */
	public PageDTO<RecommendationDTO> search(String serviceId, RecommendationFilterDTO filter) throws ServiceException {
		return delegate().search(serviceId, filter);
	}
}
