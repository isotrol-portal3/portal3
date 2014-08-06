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


import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.server.MemberCountManager;


/**
 * Implementation of MemberCountManager.
 * @author Andres Rodriguez Chamorro
 */
@Service
public final class MemberCountManagerImpl extends AbstractWeb20Service implements MemberCountManager {
	/** Validated member count. */
	private volatile Map<UUID, Integer> validatedMap = ImmutableMap.of();
	/** Non-validated member count. */
	private volatile Map<UUID, Integer> nonValidatedMap = ImmutableMap.of();

	/** Default constructor. */
	public MemberCountManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.web20.server.MemberCountManager#refresh()
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void refresh() {
		validatedMap = ImmutableMap.copyOf(getDao().getValidatedMembers());
		nonValidatedMap = ImmutableMap.copyOf(getDao().getNonValidatedMembers());
	}

	/**
	 * @see com.isotrol.impe3.web20.server.MemberCountManager#fill(java.util.UUID,
	 * com.isotrol.impe3.web20.api.CommunityDTO)
	 */
	public <T extends CommunityDTO> T fill(UUID id, T dto) {
		if (dto == null) {
			return null;
		}
		Integer count = validatedMap.get(id);
		dto.setValidatedMembers(count != null ? count.intValue() : 0);
		count = nonValidatedMap.get(id);
		dto.setNonValidatedMembers(count != null ? count.intValue() : 0);
		return dto;
	}
}
