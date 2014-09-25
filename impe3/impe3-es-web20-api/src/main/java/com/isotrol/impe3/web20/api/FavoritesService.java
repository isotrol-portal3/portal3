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
import com.isotrol.impe3.dto.ServiceException;

/**
 * Members favorites service. 
 * @author Emilio Escobar Reyero
 */
public interface FavoritesService extends Web20Service {

	/**
	 * Adds new favorite.
	 * @param serviceId External service.
	 * @param dto Favorite dto.
	 * @return Favorite id.
	 */
	Long add(String serviceId, FavoriteDTO dto) throws ServiceException;
	
	/**
	 * Removes favorite.
	 * @param serviceId External service.
	 * @param id Favorite id.
	 */
	void remove(String serviceId, Long id) throws ServiceException;
	
	/**
	 * Returns favorite dto.
	 * @param serviceId External service.
	 * @param id Favorite id.
	 * @return The favorite dto.
	 */
	FavoriteDTO getById(String serviceId, Long id) throws ServiceException;
	
	/**
	 * Returns paginated favorites list for a member on a community
	 * @param serviceId External service.
	 * @param filter The filter.
	 * @return Favorites.
	 */
	PageDTO<FavoriteDTO> getCommunityFavorites(String serviceId, FavoriteFilterDTO filter) throws ServiceException;
	
}
