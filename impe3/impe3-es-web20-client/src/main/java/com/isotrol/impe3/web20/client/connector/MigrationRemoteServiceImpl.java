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

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MigrationService;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public class MigrationRemoteServiceImpl extends WithHessian<MigrationService> implements MigrationService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class serviceClass() {
		return MigrationService.class;
	}

	@Override
	protected String serviceUrl() {
		return server() + "/migration";
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MigrationService#markDeletedAllMembers(java.lang.String)
	 */
	public void markDeletedAllMembers(String serviceId) throws ServiceException {
		delegate().markDeletedAllMembers(serviceId);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MigrationService#markeDeletedAutomaticCommunities(java.lang.String)
	 */
	public void markeDeletedAutomaticCommunities(String serviceId) throws ServiceException {
		delegate().markeDeletedAutomaticCommunities(serviceId);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MigrationService#safeCommunityCreate(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityDTO)
	 */
	public String safeCommunityCreate(String serviceId, CommunityDTO community) throws ServiceException {
		return delegate().safeCommunityCreate(serviceId, community);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MigrationService#safeMemberCreate(java.lang.String,
	 * com.isotrol.impe3.web20.api.MemberDTO)
	 */
	public String safeMemberCreate(String serviceId, MemberDTO member) throws ServiceException {
		return delegate().safeMemberCreate(serviceId, member);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MigrationService#addToCommunity(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Map, boolean)
	 */
	public void addToCommunity(String serviceId, String memberId, String communityId, String role,
		Map<String, String> properties, boolean validated) throws ServiceException {
		delegate().addToCommunity(serviceId, memberId, communityId, role, properties, validated);
	}
	
	/**
	 * @see com.isotrol.impe3.web20.api.MigrationService#updateMembersLogTable(java.lang.String)
	 */
	public void updateMembersLogTable(String serviceId) throws ServiceException {
		delegate().updateMembersLogTable(serviceId);
	}
	
	public void safeAddToGlobal(String serviceId) throws ServiceException {
		delegate().safeAddToGlobal(serviceId);
	}
	
	public MemberDTO getDeletedMemberByLastCode(String serviceId, String code) throws ServiceException {
		return delegate().getDeletedMemberByLastCode(serviceId, code);
	}
}
