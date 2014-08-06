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
package com.isotrol.impe3.web20.gui.api.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.web20.api.NoticeDTO;
import com.isotrol.impe3.web20.api.NoticeFilterDTO;

/**
 * 
 * @author Emilio Escobar Reyero.
 */
public interface INoticesServiceAsync {

	/**
	 * Returns community notices.
	 * @param serviceId External service id
	 * @param filter The filter.
	 * @param callback The community notices.
	 */
	void getCommunityNotices(String serviceId, NoticeFilterDTO filter, AsyncCallback<PageDTO<NoticeDTO>> callback);

	/**
	 * Returns notice.
	 * @param serviceId External service id
	 * @param id The notice id.
	 * @param callback The notice.
	 */
	void getById(String serviceId, Long id, AsyncCallback<NoticeDTO> callback);

	/**
	 * Creates a notice
	 * @param serviceId External service id
	 * @param dto The notice
	 * @param callback The notice id.
	 */
	void create(String serviceId, NoticeDTO dto, AsyncCallback<Long> callback);
	
	/**
	 * Updates a notice. 
	 * @param serviceId External service id.
	 * @param dto The notice.
	 * @param callback void.
	 */
	void update(String serviceId, NoticeDTO dto, AsyncCallback<Void> callback);
	
	/**
	 * Deletes notice.
	 * @param serviceId External service id.
	 * @param id The notice id.
	 * @param callback void.
	 */
	void delete(String serviceId, Long id, AsyncCallback<Void> callback);
	
}
