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
import com.isotrol.impe3.web20.api.CommunityNoticesService;
import com.isotrol.impe3.web20.api.NoticeDTO;
import com.isotrol.impe3.web20.api.NoticeFilterDTO;

/**
 * 
 * @author Emilio Escobar Reyero
 */
public class CommunityNoticesRemoteServiceImpl extends WithHessian<CommunityNoticesService> implements CommunityNoticesService  {

	@SuppressWarnings("unchecked")
	@Override
	protected Class serviceClass() {
		return CommunityNoticesService.class;
	}
	
	@Override
	protected String serviceUrl() {
		return server() + "/notices";
	}

	public Long create(String serviceId, NoticeDTO dto) throws ServiceException {
		return delegate().create(serviceId, dto);
	}
	
	public void delete(String serviceId, Long id) throws ServiceException {
		delegate().delete(serviceId, id);
	}
	
	public NoticeDTO getById(String serviceId, Long id) throws ServiceException {
		return delegate().getById(serviceId, id);
	}
	
	public PageDTO<NoticeDTO> getCommunityNotices(String serviceId, NoticeFilterDTO filter) throws ServiceException {
		return delegate().getCommunityNotices(serviceId, filter);
	}
	
	public void update(String serviceId, NoticeDTO dto) throws ServiceException {
		delegate().update(serviceId, dto);
	}
}
