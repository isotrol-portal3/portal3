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

import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.client.i18n.UsersMessages;
import com.isotrol.impe3.users.api.DuplicatePortalUserException;
import com.isotrol.impe3.users.api.PortalUserNotFoundException;

/**
 * Exception to message mapper for the Portal Users service.
 * 
 * @author Andrei Cojocaru
 *
 */
public class PortalUsersServiceErrorMessageResolver extends ExternalServiceErrorMessageResolver {

	/**
	 * Portal Users messages bundle.<br/>
	 */
	private UsersMessages uMessages = null;
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.error.AErrorMessageResolver#getMessage(java.lang.Throwable)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected String getMessage(Throwable error) {
		String res = null;
		
		if (error instanceof PortalUserNotFoundException) {
			res = uMessages.exceptionPortalUserNotFound();
		} else if (error instanceof DuplicatePortalUserException) {
			res = uMessages.exceptionDuplicatePortalUser();
		} else {
			res = super.getMessage(error);
		}
		
		return res;
	}

	/**
	 * Injects the Portal Users messages bundle.
	 * @param messages the Portal Users messages to set
	 */
	@Inject
	public void setPortalUsersMessages(UsersMessages messages) {
		this.uMessages = messages;
	}
}
