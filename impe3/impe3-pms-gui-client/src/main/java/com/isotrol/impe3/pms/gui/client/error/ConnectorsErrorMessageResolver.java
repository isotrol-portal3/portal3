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
package com.isotrol.impe3.pms.gui.client.error;

import com.isotrol.impe3.pms.api.connector.ConnectorInUseException;
import com.isotrol.impe3.pms.api.connector.ConnectorNotFoundException;

/**
 * Error message resolver for exceptions thrown by Connectors service method calls
 * 
 * @author Andrei Cojocaru
 *
 */
public class ConnectorsErrorMessageResolver extends APmsErrorMessageResolver {
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.error.SimpleErrorMessageResolver#getMessage(java.lang.Throwable)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected String getMessage(Throwable error) {
		if (error instanceof ConnectorNotFoundException) {
			return getPmsMessages().msgErrorConnectorNotFound();
		} else if (error instanceof ConnectorInUseException) {
			return getPmsMessages().msgErrorConnectorInUse();
		}
		return super.getMessage(error);
	}
}
