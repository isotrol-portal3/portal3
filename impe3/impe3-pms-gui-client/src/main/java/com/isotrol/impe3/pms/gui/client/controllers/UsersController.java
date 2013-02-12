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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.controllers;

import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.user.AuthorizationDTO;
import com.isotrol.impe3.pms.api.user.UserDTO;
import com.isotrol.impe3.pms.api.user.UserSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;

/**
 * Wrapper for the {@link IUsersServiceAsync} with event firing capabilities.
 * 
 * @author Andrei Cojocaru
 *
 */
public class UsersController extends ChangeEventSupport implements
		IUsersServiceAsync {

	/**
	 * @param service
	 */
	public UsersController(IUsersServiceAsync usersService) {
		this.service = usersService;
	}

	/**
	 * Users service proxy.<br/>
	 */
	private IUsersServiceAsync service = null;
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync
	 * #delete(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	/**
	 * Fires an event of type {@link PmsChangeEvent#DELETE}.<br/>
	 */
	public void delete(final String id, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> newCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}
			public void onSuccess(Void arg0) {
				PmsChangeEvent changeEvent = new PmsChangeEvent(PmsChangeEvent.DELETE, id);
				UsersController.this.notify(changeEvent);
				callback.onSuccess(arg0);
			}
		};
		service.delete(id, newCallback);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync
	 * #get(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	/**
	 * Forwards the method call to {@link #service}.<br/>
	 */
	public void get(String id, AsyncCallback<UserDTO> callback) {
		service.get(id, callback);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync
	 * #getGranted(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	/**
	 * Forwards the method call to {@link #service}.<br/>
	 */
	public void getGranted(String id, AsyncCallback<AuthorizationDTO> callback) {
		service.getGranted(id, callback);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync
	 * #getPortalAuthorities(java.lang.String, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	/**
	 * Forwards the method call to {@link #service}.<br/>
	 */
	public void getPortalAuthorities(String id, String portalId,
			AsyncCallback<Set<PortalAuthority>> callback) {
		service.getPortalAuthorities(id, portalId, callback);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync
	 * #getUsers(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	/**
	 * Forwards the method call to {@link #service}.<br/>
	 */
	public void getUsers(AsyncCallback<List<UserSelDTO>> callback) {
		service.getUsers(callback);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync
	 * #save(com.isotrol.impe3.pms.api.user.UserDTO, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	/**
	 * On success, fires events of type {@link PmsChangeEvent#ADD} or {@link PmsChangeEvent#UPDATE}.<br/>
	 */
	public void save(final UserDTO dto, final AsyncCallback<UserDTO> callback) {
		AsyncCallback<UserDTO> newCallback = new AsyncCallback<UserDTO>() {
			public void onSuccess(UserDTO saved) {
				int type = PmsChangeEvent.UPDATE;
				if (dto.getId() == null) {
					type = PmsChangeEvent.ADD;
				}
				PmsChangeEvent changeEvent = new PmsChangeEvent(type, saved);
				UsersController.this.notify(changeEvent);
				callback.onSuccess(saved);
			}
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}
		};
		service.save(dto, newCallback);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync
	 * #setPassword(java.lang.String, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	/**
	 * Forwards the method call to {@link #service}.<br/>
	 */
	public void setPassword(String id, String password, AsyncCallback<Void> callback) {
		service.setPassword(id, password, callback);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync
	 * #setPortalAuthorities(java.lang.String, java.lang.String, java.util.Set, 
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	/**
	 * Forwards the method call to {@link #service}.<br/>
	 */
	public void setPortalAuthorities(String id, String portalId,
			Set<PortalAuthority> granted, AsyncCallback<Void> callback) {
		service.setPortalAuthorities(id, portalId, granted, callback);
	}

}
