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
package com.isotrol.impe3.web20.dao;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.web20.api.CommunityMembersFilterDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MemberSelDTO;

/**
 * Members Indexed DAO.
 * @author Emilio Escobar Reyero
 */
public interface MemberIdxDAO {

	/**
	 * Find members.
	 * @param filter Search filter.
	 * @return A page of results.
	 */
	PageDTO<MemberSelDTO> findMembers(PageFilter<MemberFilterDTO> filter);

	/**
	 * Returns members ids.
	 * @param filter Search filter.
	 * @return A page of results.
	 */
	PageDTO<String> findMemberIds(PageFilter<MemberFilterDTO> filter);

	
	/**
	 * Find members on a community.
	 * @param filter Search filter.
	 * @return A page of members uuids.
	 */
	PageDTO<String> findCommunityMembers(PageFilter<CommunityMembersFilterDTO> filter);
	
}
