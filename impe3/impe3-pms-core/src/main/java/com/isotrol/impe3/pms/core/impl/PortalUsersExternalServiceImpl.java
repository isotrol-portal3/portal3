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

package com.isotrol.impe3.pms.core.impl;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService;
import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.api.PortalUserException;
import com.isotrol.impe3.users.api.PortalUserNotFoundException;
import com.isotrol.impe3.users.api.PortalUserSelDTO;
import com.isotrol.impe3.users.api.PortalUsersService;


/**
 * Implementation of PortalUsersExternalService.
 * @author Andres Rodriguez.
 */
@Service("portalUsersExternalService")
public final class PortalUsersExternalServiceImpl extends AbstractExternalService<PortalUsersService> implements
	PortalUsersExternalService {
	/** Default constructor. */
	public PortalUsersExternalServiceImpl() {
		super(PortalUsersService.class);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService#changePassword(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void changePassword(String serviceId, String id, String password) throws PortalUserNotFoundException,
		PMSException {
		getExternalService(serviceId).changePassword(id, password);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService#checkPassword(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO checkPassword(String serviceId, String username, String password) throws PMSException {
		return getExternalService(serviceId).checkPassword(username, password);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService#create(java.lang.String,
	 * com.isotrol.impe3.users.api.PortalUserDTO, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO create(String serviceId, PortalUserDTO user, String password) throws PortalUserException,
		PMSException {
		return getExternalService(serviceId).create(user, password);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService#delete(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, String id) throws PortalUserNotFoundException, PMSException {
		getExternalService(serviceId).delete(id);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService#getByEMail(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO getByEMail(String serviceId, String email) throws PMSException {
		return getExternalService(serviceId).getByEMail(email);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService#getById(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO getById(String serviceId, String id) throws PortalUserNotFoundException, PMSException {
		return getExternalService(serviceId).getById(id);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService#getByName(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO getByName(String serviceId, String name) throws PMSException {
		return getExternalService(serviceId).getByName(name);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService#getUsers(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<PortalUserSelDTO> getUsers(String serviceId) throws PMSException {
		return getExternalService(serviceId).getUsers();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.PortalUsersExternalService#update(java.lang.String,
	 * com.isotrol.impe3.users.api.PortalUserDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO update(String serviceId, PortalUserDTO user) throws PortalUserException, PMSException {
		return getExternalService(serviceId).update(user);
	}
}
