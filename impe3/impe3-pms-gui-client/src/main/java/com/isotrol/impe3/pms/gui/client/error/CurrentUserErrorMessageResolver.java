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

import com.isotrol.impe3.pms.api.AuthorizationException;


/**
 * Error message resolver for exceptions thrown by Sessions service method calls
 * 
 */
public class CurrentUserErrorMessageResolver extends APmsErrorMessageResolver {

	/**
	 * @see com.isotrol.impe3.pms.gui.client.error.SimpleErrorMessageResolver#getMessage(java.lang.Throwable)
	 */
	@Override
	protected String getMessage(Throwable error) {
		
		if (error instanceof AuthorizationException) {
			return getPmsMessages().msgErrorPasswordWrong();
		}
		
		return super.getMessage(error);
	}
}
