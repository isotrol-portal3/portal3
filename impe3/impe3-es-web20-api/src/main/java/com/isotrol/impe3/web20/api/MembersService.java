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

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;


/**
 * Members Service.
 * @author Emilio Escobar Reyero
 */
public interface MembersService extends Web20Service {

	/**
	 * Search members.
	 * @param serviceId External service id.
	 * @param filter Search filter.
	 * @return A page of results.
	 */
	PageDTO<MemberSelDTO> search(String serviceId, PageFilter<MemberFilterDTO> filter) throws ServiceException;

	/**
	 * Gets the detail of a member.
	 * @param serviceId External service id.
	 * @param id Member id.
	 * @return The member details.
	 * @throws MemberNotFoundException if the member with the specified id is not found.
	 */
	MemberDTO getById(String serviceId, String id) throws ServiceException;

	/**
	 * Gets the detail of a member.
	 * @param serviceId External service id.
	 * @param code Member code.
	 * @return The user details or {@code null} if not found.
	 */
	MemberDTO getByCode(String serviceId, String code) throws ServiceException;

	/**
	 * Gets the detail of a member.
	 * @param serviceId External service id.
	 * @param name Member name.
	 * @return The user details or {@code null} if not found.
	 */
	MemberDTO getByName(String serviceId, String name) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param filter
	 * @return
	 * @throws ServiceException
	 */
	PageDTO<MembershipSelDTO> getMemberships(String serviceId, PageFilter<MembershipSelFilterDTO> filter) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param member
	 * @return
	 * @throws ServiceException
	 */
	String create(String serviceId, MemberDTO member) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param member
	 * @throws ServiceException
	 */
	void update(String serviceId, MemberDTO member) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param id
	 * @throws ServiceException
	 */
	void delete(String serviceId, String id) throws ServiceException;

	/**
	 * Adds member to community.
	 * @param serviceId External service id.
	 * @param memberId The member id.
	 * @param communityId The community id.
	 * @param role The member role.
	 * @param properties Membership properties map.
	 * @param validated Membership state.
	 * @throws ServiceException
	 */
	void addToCommunity(String serviceId, String memberId, String communityId, String role,
		Map<String, String> proerties, boolean validated) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param memberId
	 * @param communityId
	 * @throws ServiceException
	 */
	void removeFromCommunity(String serviceId, String memberId, String communityId) throws ServiceException;
}
