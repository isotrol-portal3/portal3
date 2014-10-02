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


import java.util.Map;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MemberSelDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public class MembersRemoteServiceImpl extends WithHessian<MembersService> implements MembersService {

	@Override
	protected Class<MembersService> serviceClass() {
		return MembersService.class;
	}

	@Override
	protected String serviceUrl() {
		return server() + "/members";
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#getById(java.lang.String, java.lang.String)
	 */
	public MemberDTO getById(String serviceId, String id) throws ServiceException {
		return delegate().getById(serviceId, id);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#getByCode(java.lang.String, java.lang.String)
	 */
	public MemberDTO getByCode(String serviceId, String code) throws ServiceException {
		return delegate().getByCode(serviceId, code);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#getByName(java.lang.String, java.lang.String)
	 */
	public MemberDTO getByName(String serviceId, String name) throws ServiceException {
		return delegate().getByName(serviceId, name);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#search(java.lang.String, com.isotrol.impe3.dto.PageFilter)
	 */
	public PageDTO<MemberSelDTO> search(String serviceId, PageFilter<MemberFilterDTO> filter) throws ServiceException {
		return delegate().search(serviceId, filter);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#create(java.lang.String, com.isotrol.impe3.web20.api.MemberDTO)
	 */
	public String create(String serviceId, MemberDTO member) throws ServiceException {
		return delegate().create(serviceId, member);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#delete(java.lang.String, java.lang.String)
	 */
	public void delete(String serviceId, String id) throws ServiceException {
		delegate().delete(serviceId, id);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#update(java.lang.String, com.isotrol.impe3.web20.api.MemberDTO)
	 */
	public void update(String serviceId, MemberDTO member) throws ServiceException {
		delegate().update(serviceId, member);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#getMemberships(java.lang.String, com.isotrol.impe3.web20.api.MembershipSelFilterDTO)
	 */
	public PageDTO<MembershipSelDTO> getMemberships(String serviceId, PageFilter<MembershipSelFilterDTO> filter)
		throws ServiceException {
		return delegate().getMemberships(serviceId, filter);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#addToCommunity(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Map, boolean)
	 */
	public void addToCommunity(String serviceId, String memberId, String communityId, String role,
		Map<String, String> properties, boolean validated) throws ServiceException {
		delegate().addToCommunity(serviceId, memberId, communityId, role, properties, validated);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#removeFromCommunity(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void removeFromCommunity(String serviceId, String memberId, String communityId) throws ServiceException {
		delegate().removeFromCommunity(serviceId, memberId, communityId);
	}

}
