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

package com.isotrol.impe3.gui.common.error;

/**
 * Base class for error message resolvers. Resolves the exception-specific message, and
 * appends it to the operation-specific message.
 * 
 * @author Andrei Cojocaru
 *
 */
public abstract class AErrorMessageResolver implements IErrorMessageResolver {

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.error.IErrorMessageResolver
	 * #getMessage(java.lang.Throwable, java.lang.String)
	 */
	/**
	 * Returns the passed OP message, followed by a line break and the message for the passed error.<br/>
	 */
	public final String getMessage(Throwable error, String opMessage) {
		return opMessage + "<br/>" + getMessage(error);
	}

	/**
	 * Returns a human-readable message that explains the passed exception.<br/>
	 * @param error the exception object
	 * @return a human-readable message that explains the passed exception.<br/>
	 */
	protected abstract String getMessage(Throwable error);
	
}
