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
import java.util.Set;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.CommunityFilterDTO;
import com.isotrol.impe3.web20.api.CommunityMemberSelDTO;
import com.isotrol.impe3.web20.api.CommunityMembersFilterDTO;
import com.isotrol.impe3.web20.api.CommunitySelDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;


/**
 * Remote communities service implementation.
 * @author Emilio Escobar Reyero
 */
public class CommunitiesRemoteServiceImpl extends WithHessian<CommunitiesService> implements CommunitiesService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class serviceClass() {
		return CommunitiesService.class;
	}

	@Override
	protected String serviceUrl() {
		return server() + "/communities";
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#getById(java.lang.String, java.lang.String)
	 */
	public CommunityDTO getById(String serviceId, String id) throws ServiceException {
		return delegate().getById(serviceId, id);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#getByCode(java.lang.String, java.lang.String)
	 */
	public CommunityDTO getByCode(String serviceId, String code) {
		return delegate().getByCode(serviceId, code);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#search(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityFilterDTO)
	 */
	public PageDTO<CommunityDTO> search(String serviceId, PageFilter<CommunityFilterDTO> filter) throws ServiceException {
		return delegate().search(serviceId, filter);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#create(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityDTO)
	 */
	public String create(String serviceId, CommunityDTO community) throws ServiceException {
		return delegate().create(serviceId, community);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#delete(java.lang.String, java.lang.String)
	 */
	public void delete(String serviceId, String id) throws ServiceException {
		delegate().delete(serviceId, id);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#update(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityDTO)
	 */
	public void update(String serviceId, CommunityDTO community) throws ServiceException {
		delegate().update(serviceId, community);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#getCommunityMembers(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityMembersFilterDTO)
	 */
	public PageDTO<CommunityMemberSelDTO> getCommunityMembers(String serviceId, PageFilter<CommunityMembersFilterDTO> filter)
		throws ServiceException {
		return delegate().getCommunityMembers(serviceId, filter);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#getCommunitiesById(java.lang.String, java.util.Set)
	 */
	public Map<String, CommunityDTO> getCommunitiesById(String serviceId, Set<String> ids) throws ServiceException {
		return delegate().getCommunitiesById(serviceId, ids);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#findCommunities(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityFilterDTO, java.lang.String)
	 */
	public PageDTO<CommunitySelDTO> findCommunities(String serviceId, PageFilter<CommunityFilterDTO> filter, String role)
		throws ServiceException {
		return delegate().findCommunities(serviceId, filter, role);
	}
	
	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#addMembers(java.lang.String, java.lang.String, com.isotrol.impe3.web20.api.MemberFilterDTO, java.lang.String, boolean)
	 */
	public void addMembers(String serviceId, String communityId, MemberFilterDTO filter, String role, boolean validated, boolean keep)
		throws ServiceException {
		delegate().addMembers(serviceId, communityId, filter, role, validated, keep);
	}
	
	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#removeMembers(java.lang.String, com.isotrol.impe3.web20.api.CommunityMembersFilterDTO)
	 */
	public void removeMembers(String serviceId, CommunityMembersFilterDTO filter) throws ServiceException {
		delegate().removeMembers(serviceId, filter);
	}
}
