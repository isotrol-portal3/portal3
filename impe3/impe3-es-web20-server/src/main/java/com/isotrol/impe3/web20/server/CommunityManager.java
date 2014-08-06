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
package com.isotrol.impe3.web20.server;


import java.util.UUID;

import com.google.common.base.Function;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.MembershipEntity;


/**
 * Internal Community Manager.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public interface CommunityManager {
	/** Global community ID. */
	UUID GLOBAL_ID = new UUID(0L, 0L);

	/** Global community string id. */
	String GLOBAL_STR_ID = GLOBAL_ID.toString();

	/**
	 * Returns the global community, creating it if needed.
	 * @return The global community.
	 */
	CommunityEntity getGlobalCommunity();
	
	/**
	 * Returns the mapper to community DTO.
	 * @return The requested mapper.
	 */
	Function<CommunityEntity, CommunityDTO> toCommunityDTO();
	
	/**
	 * Returns the mapper to MembershipSelDTO.
	 * @return The requested mapper.
	 */
	Function<MembershipEntity, MembershipSelDTO> toMembershipSelDTO();
}
