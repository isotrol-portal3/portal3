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

import com.isotrol.impe3.dto.ServiceException;


/**
 * Members and communities migrator helper.
 * @author Emilio Escobar Reyero
 */
public interface MigrationService extends Web20Service {

	/**
	 * Deleted all members
	 * @param serviceId External service id
	 */
	void markDeletedAllMembers(String serviceId) throws ServiceException;

	/**
	 * Create (or recover) new member.
	 * @param serviceId External service id
	 * @param member The member DTO.
	 * @return The member uuid.
	 */
	String safeMemberCreate(String serviceId, MemberDTO member) throws ServiceException;

	/**
	 * Delete all automatic communities.
	 * @param serviceId External service id
	 */
	void markeDeletedAutomaticCommunities(String serviceId) throws ServiceException;

	/**
	 * Create (or recover) new member.
	 * @param serviceId External service id
	 * @param community The community dTO.
	 * @return The community uuid.
	 */
	String safeCommunityCreate(String serviceId, CommunityDTO community) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param memberId
	 * @param communityId
	 * @param role
	 * @param properties
	 * @param validated
	 * @throws ServiceException
	 */
	void addToCommunity(String serviceId, String memberId, String communityId, String role,
		Map<String, String> properties, boolean validated) throws ServiceException;
	
	/**
	 * 
	 * @param serviceId
	 * @throws ServiceException
	 */
	void updateMembersLogTable(String serviceId) throws ServiceException;
	
	
	void safeAddToGlobal(String serviceId) throws ServiceException;
	
	
	MemberDTO getDeletedMemberByLastCode(String serviceId, String code) throws ServiceException;
}
