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
package com.isotrol.impe3.web20.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;
import com.isotrol.impe3.web20.server.CommunityManager;
import com.isotrol.impe3.web20.server.MembershipManager;

/**
 * Membership manager implementation.
 * @author Emilio Escobar Reyero
 */
@Service("membershipManager")
public class MembershipManagerImpl extends AbstractWeb20Service implements MembershipManager {

	/** Community manager. */
	@Autowired
	private CommunityManager communityManager;

	
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<MembershipSelDTO> getMemberships(String serviceId, PageFilter<MembershipSelFilterDTO> filter)
		throws ServiceException {
		Preconditions.checkNotNull(filter);
		Preconditions.checkNotNull(filter.getFilter());
		
		filter.setFilter(filter.getFilter().putGlobal(true));
		
		return getDao().getMemberMemberships(filter, communityManager.toMembershipSelDTO());
	}
	
}
