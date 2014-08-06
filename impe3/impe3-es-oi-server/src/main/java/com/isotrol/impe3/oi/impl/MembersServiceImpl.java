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
package com.isotrol.impe3.oi.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.oi.api.MembersService;
import com.isotrol.impe3.oi.model.OIMemberEntity;

/**
 * Members service implementation.
 * @author Emilio Escobar Reyero
 */
@Service("oiMembersService")
public class MembersServiceImpl extends AbstractOiService implements MembersService {
	/** Member component. */
	@Autowired
	private OIMemberComponent memberComponent;
	
	/**
	 * @see com.isotrol.impe3.oi.api.MembersService#activate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Long activate(String serviceId, String member, String displayName) throws ServiceException {
		final Long id = memberComponent.getMember(member);
		final OIMemberEntity entity = getDao().findById(OIMemberEntity.class, id, false);
		entity.setDeleted(false);
		entity.setDisplayName(displayName);
		return entity.getId();
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.MembersService#delete(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, String member) throws ServiceException {
		final Long id = memberComponent.getMember(member);
		final OIMemberEntity entity = getDao().findById(OIMemberEntity.class, id, false);
		entity.setDeleted(true);
	}
	
	public void setMemberComponent(OIMemberComponent memberComponent) {
		this.memberComponent = memberComponent;
	}
}
