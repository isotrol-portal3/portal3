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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.api.RoutingDomain;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.rd.RoutingDomainDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainSelDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainsService;
import com.isotrol.impe3.pms.core.obj.Context0;
import com.isotrol.impe3.pms.core.obj.RoutingDomainObject;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.RoutingDomainEntity;


/**
 * Implementation of RoutingDomainsService.
 * @author Andres Rodriguez.
 */
@Service("routingDomainsService")
public final class RoutingDomainsServiceImpl extends AbstractOfEnvironmentService<RoutingDomainEntity> implements
	RoutingDomainsService {

	/** Default constructor. */
	public RoutingDomainsServiceImpl() {
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.ROUTING_DOMAIN;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.rd.RoutingDomainsService#getDomains()
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<RoutingDomainSelDTO> getDomains() throws PMSException {
		return loadContext0().getDomains().map2sel(false);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.rd.RoutingDomainsService#get(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.RD_GET)
	public RoutingDomainDTO get(String id) throws PMSException {
		return loadContext0().getDomains().loadNotDefault(id).toDTO();
	}

	private void touch() throws PMSException {
		getEnvironment().touchDomainVersion(loadUser());
	}

	private RoutingDomainEntity fetch(RoutingDomainObject rdo) {
		return findById(RoutingDomainEntity.class, rdo.getId());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.rd.RoutingDomainsService#save(com.isotrol.impe3.pms.api.rd.RoutingDomainDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.RD_GET)
	public RoutingDomainDTO save(RoutingDomainDTO dto) throws PMSException {
		checkNotNull(dto);
		final Context0 ctx = loadContext0();
		String id = dto.getId();
		final boolean isNew = id == null;
		final RoutingDomainEntity entity;
		if (isNew) {
			entity = new RoutingDomainEntity();
		} else {
			entity = fetch(ctx.getDomains().loadNotDefault(id));
		}
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setOnlineBase(dto.getOnlineBase());
		entity.setOnlineAbsBase(dto.getOnlineAbsBase());
		RoutingDomain drd = ctx.getDomains().loadDefault().getDomain();
		entity.setOfflineBase(drd.getOffline().getBase().toASCIIString());
		entity.setOfflineAbsBase(drd.getOffline().getAbsoluteBase().toASCIIString());
		if (isNew) {
			saveNew(entity);
			id = entity.getId().toString();
		} else {
			entity.setUpdated(loadUser());
		}
		touch();
		sync();
		return get(id);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.rd.RoutingDomainsService#delete(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.RD_SET)
	public void delete(String id) throws PMSException {
		// TODO
	}

	/**
	 * @see com.isotrol.impe3.pms.api.rd.RoutingDomainsService#getDefault()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.RD_GET)
	public RoutingDomainDTO getDefault() throws PMSException {
		return loadContext0().getDomains().loadDefault().toDTO();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.rd.RoutingDomainsService#getDefault()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.RD_SET)
	public RoutingDomainDTO setDefault(RoutingDomainDTO dto) throws PMSException {
		final Context0 ctx = loadContext0();
		final RoutingDomainEntity entity = fetch(ctx.getDomains().loadDefault());
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setOnlineBase(dto.getOnlineBase());
		entity.setOfflineBase(dto.getOfflineBase());
		entity.setOnlineAbsBase(dto.getOnlineAbsBase());
		entity.setOfflineAbsBase(dto.getOfflineAbsBase());
		touch();
		sync();
		return getDefault();
	}
}
