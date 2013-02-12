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

import com.isotrol.impe3.pms.api.edition.ModifiedGlobalsException;
import com.isotrol.impe3.pms.api.portal.PortalInUseException;
import com.isotrol.impe3.pms.api.portal.PortalNotFoundException;

/**
 * Error message resolver for the Portals service.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 *
 */
public class PortalsServiceErrorMessageResolver extends APmsErrorMessageResolver {
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.error.SimpleErrorMessageResolver#getMessage(java.lang.Throwable)
	 */
	/**
	 * <br/>
	 */
	@Override
	public String getMessage(Throwable error) {
		if (error instanceof PortalNotFoundException) {
			return getPmsMessages().exceptionPortalNotFound();
		} else if (error instanceof PortalInUseException) {
			return getPmsMessages().exceptionPortalInUse();
		} else if (error instanceof ModifiedGlobalsException) {
			return getPmsMessages().exceptionModifiedGlobals();
		}
		return super.getMessage(error);
	}
}
