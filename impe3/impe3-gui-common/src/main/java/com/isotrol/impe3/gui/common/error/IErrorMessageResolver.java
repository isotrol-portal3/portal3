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
 * Represents classes with the ability of translating an error to a human readable internationalized message.
 * 
 * @author Andrei Cojocaru
 *
 */
public interface IErrorMessageResolver {
	/**
	 * Returns the error message that corresponds to the passed error.<br/>
	 * @param error thrown error
	 * @param opMessage message that contains information about the operation that failed. Example: <br/>
	 * "The category could not be saved".<br>
	 * This message will be shown first, before a message that contains information about the particular exception type.
	 * 
	 * @return the error message that corresponds to the passed error.<br/>
	 */
	String getMessage(Throwable error, String opMessage);
}
