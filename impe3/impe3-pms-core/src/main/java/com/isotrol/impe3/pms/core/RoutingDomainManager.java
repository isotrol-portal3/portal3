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

package com.isotrol.impe3.pms.core;


import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.obj.RoutingDomainsObject;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.RoutingDomainEntity;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Routing domain managers.
 * @author Andres Rodriguez.
 */
public interface RoutingDomainManager extends GenericManager<RoutingDomainsObject> {
	/** Default routing domain name. */
	String DEFAULT = "DEFAULT";
	/** Default base URL. */
	String BASE = "http://localhost:8080/impe3";

	/**
	 * Gets the default routing domain, creating it if needed.
	 * @param e Environment.
	 * @param user Current user.
	 * @return The default routing domain.
	 */
	RoutingDomainEntity getDefault(EnvironmentEntity e, UserEntity user) throws PMSException;
}
