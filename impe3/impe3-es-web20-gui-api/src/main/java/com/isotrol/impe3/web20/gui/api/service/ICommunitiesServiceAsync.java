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


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.web20.api.CommunityFilterDTO;
import com.isotrol.impe3.web20.api.CommunityMembersFilterDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;


/**
 * @author Manuel Ruiz
 * 
 */
public interface ICommunitiesServiceAsync {

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void search(java.lang.String serviceId, PageFilter<CommunityFilterDTO> filter,
		AsyncCallback<com.isotrol.impe3.dto.PageDTO<com.isotrol.impe3.web20.api.CommunityDTO>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void getById(java.lang.String serviceId, java.lang.String id,
		AsyncCallback<com.isotrol.impe3.web20.api.CommunityDTO> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void getByCode(java.lang.String serviceId, java.lang.String code,
		AsyncCallback<com.isotrol.impe3.web20.api.CommunityDTO> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void create(java.lang.String serviceId, com.isotrol.impe3.web20.api.CommunityDTO community,
		AsyncCallback<java.lang.String> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void update(java.lang.String serviceId, com.isotrol.impe3.web20.api.CommunityDTO community,
		AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void delete(java.lang.String serviceId, java.lang.String id, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void getCommunityMembers(java.lang.String serviceId, PageFilter<CommunityMembersFilterDTO> filter,
		AsyncCallback<com.isotrol.impe3.dto.PageDTO<com.isotrol.impe3.web20.api.CommunityMemberSelDTO>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void getCommunitiesById(java.lang.String serviceId, java.util.Set<java.lang.String> ids,
		AsyncCallback<java.util.Map<java.lang.String, com.isotrol.impe3.web20.api.CommunityDTO>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void findCommunities(java.lang.String serviceId, PageFilter<CommunityFilterDTO> filter, java.lang.String role,
		AsyncCallback<com.isotrol.impe3.dto.PageDTO<com.isotrol.impe3.web20.api.CommunitySelDTO>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void addMembers(String serviceId, String communityId, MemberFilterDTO filter, String role, boolean validated,
		boolean keep, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.ICommunitiesService
	 */
	void removeMembers(String serviceId, CommunityMembersFilterDTO filter, AsyncCallback<Void> callback);

	/**
	 * Utility class to get the RPC Async interface from client-side code
	 */
	public static class Util {
		private static ICommunitiesServiceAsync instance;

		private Util() {

		}

		/**
		 * @return the instance
		 */
		public static ICommunitiesServiceAsync getInstance() {
			if (instance == null) {
				instance = (ICommunitiesServiceAsync) GWT.create(ICommunitiesService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "ICommunitiesService");
			}
			return instance;
		}
	}
}
