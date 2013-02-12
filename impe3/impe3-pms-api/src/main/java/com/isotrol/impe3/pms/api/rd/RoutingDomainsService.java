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

package com.isotrol.impe3.pms.api.rd;


import java.util.List;

import com.isotrol.impe3.pms.api.PMSException;


/**
 * Routing domains service.
 * @author Andres Rodriguez.
 */
public interface RoutingDomainsService {
	/**
	 * Returns the routing domains.
	 * @return The routing domains.
	 */
	List<RoutingDomainSelDTO> getDomains() throws PMSException;

	/**
	 * Returns the detail of a routing domain.
	 * @param id Routing domain id.
	 * @return The requested routing domain.
	 * @throws PMSException
	 */
	RoutingDomainDTO get(String id) throws PMSException;

	/**
	 * Saves a routing domain. If the id is {@code null} it is considered an insert, otherwise it is an update.
	 * @param dto Routing domain to save.
	 * @return The saved routing domain.
	 * @throws PMSException
	 */
	RoutingDomainDTO save(RoutingDomainDTO dto) throws PMSException;

	/**
	 * Deletes a routing domain.
	 * @param id Routing domain id.
	 * @throws PMSException
	 */
	void delete(String id) throws PMSException;

	/**
	 * Returns the default routing domain.
	 * @return The default routing domain.
	 * @throws PMSException
	 */
	RoutingDomainDTO getDefault() throws PMSException;

	/**
	 * Sets the default routing domain
	 * @param dto The default routing domain.
	 * @return The default routing domain.
	 * @throws PMSException
	 */
	RoutingDomainDTO setDefault(RoutingDomainDTO dto) throws PMSException;
}
