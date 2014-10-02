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
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.FavoriteDTO;
import com.isotrol.impe3.web20.api.FavoriteFilterDTO;
import com.isotrol.impe3.web20.api.FavoritesService;

/**
 * 
 * @author Emilio Escobar Reyero
 */
public class FavoritesRemoteServiceImpl extends WithHessian<FavoritesService> implements FavoritesService  {

	@Override
	protected Class<FavoritesService> serviceClass() {
		return FavoritesService.class;
	}
	
	@Override
	protected String serviceUrl() {
		return server() + "/favorites";
	}

	/**
	 * @see com.isotrol.impe3.web20.api.FavoritesService#getById(java.lang.String, java.lang.Long)
	 */
	public FavoriteDTO getById(String serviceId, Long id) throws ServiceException {
		return delegate().getById(serviceId, id);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.FavoritesService#add(java.lang.String, com.isotrol.impe3.web20.api.FavoriteDTO)
	 */
	public Long add(String serviceId, FavoriteDTO dto) throws ServiceException {
		return delegate().add(serviceId, dto);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.FavoritesService#getCommunityFavorites(java.lang.String, com.isotrol.impe3.web20.api.FavoriteFilterDTO)
	 */
	public PageDTO<FavoriteDTO> getCommunityFavorites(String serviceId, FavoriteFilterDTO filter)
		throws ServiceException {
		return delegate().getCommunityFavorites(serviceId, filter);
	}
	
	/**
	 * @see com.isotrol.impe3.web20.api.FavoritesService#remove(java.lang.String, java.lang.Long)
	 */
	public void remove(String serviceId, Long id) throws ServiceException {
		delegate().remove(serviceId, id);		
	}
	
	
}
