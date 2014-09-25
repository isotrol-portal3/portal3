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


import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;


/**
 * Communities Service.
 * @author Emilio Escobar Reyero
 */
public interface CommunitiesService extends Web20Service {

	/**
	 * Search communities.
	 * @param serviceId External service id.
	 * @param filter Search filter.
	 * @return A page of results.
	 */
	PageDTO<CommunityDTO> search(String serviceId, PageFilter<CommunityFilterDTO> filter) throws ServiceException;

	/**
	 * Gets the detail of a community.
	 * @param serviceId External service id.
	 * @param id Community id.
	 * @return The community details.
	 * @throws CommunityNotFoundException if the community with the specified id is not found.
	 */
	CommunityDTO getById(String serviceId, String id) throws ServiceException;

	/**
	 * Gets the detail of a community.
	 * @param serviceId External service id.
	 * @param code Community code.
	 * @return The community details or {@code null} if not found.
	 */
	CommunityDTO getByCode(String serviceId, String code);

	/**
	 * 
	 * @param serviceId
	 * @param community
	 * @return
	 * @throws ServiceException
	 */
	String create(String serviceId, CommunityDTO community) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param community
	 * @throws ServiceException
	 */
	void update(String serviceId, CommunityDTO community) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param id
	 * @throws ServiceException
	 */
	void delete(String serviceId, String id) throws ServiceException;
	
	/**
	 * Returns the community members.
	 * @param serviceId External service id.
	 * @param communityId Community id.
	 * @return A page of results.
	 * @throws ServiceException
	 */
	PageDTO<CommunityMemberSelDTO> getCommunityMembers(String serviceId, PageFilter<CommunityMembersFilterDTO> filter) throws ServiceException;
	
	/**
	 * Gets the detail of a set of communities.
	 * @param serviceId External service id
	 * @param ids The set of ids.
	 * @return The communities details.
	 * @throws ServiceException
	 */
	Map<String, CommunityDTO> getCommunitiesById(String serviceId, Set<String> ids) throws ServiceException; 
	
	/**
	 * Search communities, plus members with a role. VERY SLOW METHOD!.
	 * @param serviceId External service id
	 * @param filter
	 * @param role
	 * @return
	 * @throws ServiceException
	 */
	PageDTO<CommunitySelDTO> findCommunities(String serviceId, PageFilter<CommunityFilterDTO> filter, String role) throws ServiceException;
	
	/**
	 * Searchs members that matches with the filter and aren't community members jet. Nothing to do if global community.
	 * @param serviceId External service id
	 * @param communityId The community id.
	 * @param filter The filter.
	 * @param role The membership role.
	 * @param validated Default validated value.
	 * @param keep Maintain validated value.
	 */
	void addMembers(String serviceId, String communityId, MemberFilterDTO filter, String role, boolean validated, boolean keep) throws ServiceException;
	
	/**
	 * Removes members than matches with filter.
	 * @param serviceId External service id
	 * @param filter The filter.
	 * @throws ServiceException
	 */
	void removeMembers(String serviceId, CommunityMembersFilterDTO filter) throws ServiceException;
}
