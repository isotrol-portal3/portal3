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

package com.isotrol.impe3.web20.api;


import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilterDTO;
import com.isotrol.impe3.dto.ServiceException;


/**
 * Resources recommendations service.
 * @author Emilio Escobar Reyero
 */
public interface RecommendationsService extends Web20Service {

	/**
	 * 
	 * @param serviceId
	 * @param recommendation
	 * @return
	 * @throws ServiceException
	 */
	Long recommendResource(String serviceId, RecommendationDTO recommendation) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param recommendation
	 * @throws ServiceException
	 */
	void removeRecommendation(String serviceId, Long recommendation) throws ServiceException;
	
	/**
	 * 
	 * @param serviceId
	 * @param filter
	 * @return
	 * @throws ServiceException
	 */
	PageDTO<RecommendationDTO> search(String serviceId, RecommendationFilterDTO filter) throws ServiceException;
	
	/**
	 * 
	 * @param serviceId
	 * @param member
	 * @param community
	 * @param pagination
	 * @return
	 * @throws ServiceException
	 */
	PageDTO<RecommendationDTO> getRecommendedResources(String serviceId, String member, String community, PageFilterDTO pagination) throws ServiceException;
}
