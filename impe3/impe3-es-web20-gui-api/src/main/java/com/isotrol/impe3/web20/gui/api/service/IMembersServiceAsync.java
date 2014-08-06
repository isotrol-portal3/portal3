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


import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;


/**
 * @author Manuel Ruiz
 *
 */
public interface IMembersServiceAsync {

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void search(java.lang.String serviceId, PageFilter<MemberFilterDTO> filter,
		AsyncCallback<com.isotrol.impe3.dto.PageDTO<com.isotrol.impe3.web20.api.MemberSelDTO>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void getById(java.lang.String serviceId, java.lang.String id,
		AsyncCallback<com.isotrol.impe3.web20.api.MemberDTO> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void getByCode(java.lang.String serviceId, java.lang.String code,
		AsyncCallback<com.isotrol.impe3.web20.api.MemberDTO> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void getByName(java.lang.String serviceId, java.lang.String name,
		AsyncCallback<com.isotrol.impe3.web20.api.MemberDTO> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void getMemberships(java.lang.String serviceId, PageFilter<MembershipSelFilterDTO> filter,
		AsyncCallback<com.isotrol.impe3.dto.PageDTO<com.isotrol.impe3.web20.api.MembershipSelDTO>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void create(java.lang.String serviceId, com.isotrol.impe3.web20.api.MemberDTO member,
		AsyncCallback<java.lang.String> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void update(java.lang.String serviceId, com.isotrol.impe3.web20.api.MemberDTO member, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void delete(java.lang.String serviceId, java.lang.String id, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void addToCommunity(java.lang.String serviceId, java.lang.String memberId, java.lang.String communityId,
		java.lang.String role, Map<String, String> proerties, boolean validated, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * @see com.isotrol.impe3.web20.gui.api.service.IMembersService
	 */
	void removeFromCommunity(java.lang.String serviceId, java.lang.String memberId, java.lang.String communityId,
		AsyncCallback<Void> callback);

	/**
	 * Utility class to get the RPC Async interface from client-side code
	 */
	public static class Util {
		private static IMembersServiceAsync instance;

		private Util() {
			
		}
		
		/**
		 * @return the instance
		 */
		public static IMembersServiceAsync getInstance() {
			if (instance == null) {
				instance = (IMembersServiceAsync) GWT.create(IMembersService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "IMembersService");
			}
			return instance;
		}
	}
}
