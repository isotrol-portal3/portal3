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
 * Community notices service.
 * @author Emilio Escobar Reyero
 *
 */
public interface CommunityNoticesService extends Web20Service {

	/**
	 * Returns community notices.
	 * @param serviceId External service id
	 * @param filter The filter.
	 * @return The community notices.
	 */
	PageDTO<NoticeDTO> getCommunityNotices(String serviceId, NoticeFilterDTO filter) throws ServiceException;

	/**
	 * Returns notice.
	 * @param serviceId External service id
	 * @param id The notice id.
	 * @return The notice.
	 */
	NoticeDTO getById(String serviceId, Long id) throws ServiceException;

	/**
	 * Creates a notice
	 * @param serviceId External service id
	 * @param dto The notice
	 * @return The notice id.
	 */
	Long create(String serviceId, NoticeDTO dto) throws ServiceException;
	
	/**
	 * Updates a notice. 
	 * @param serviceId External service id.
	 * @param dto The notice.
	 */
	void update(String serviceId, NoticeDTO dto) throws ServiceException;
	
	/**
	 * Deletes notice.
	 * @param serviceId External service id.
	 * @param id The notice id.
	 */
	void delete(String serviceId, Long id) throws ServiceException;
}
