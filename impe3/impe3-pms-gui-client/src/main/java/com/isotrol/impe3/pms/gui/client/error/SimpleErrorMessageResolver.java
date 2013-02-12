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

package com.isotrol.impe3.pms.gui.client.error;


import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.error.AErrorMessageResolver;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.pms.api.AuthorizationException;
import com.isotrol.impe3.pms.api.InvalidOperationException;
import com.isotrol.impe3.pms.api.NoSessionException;

/**
 * Error message resolver for exceptions that do not need any extra info, and may be thrown during
 * any service call.<br/>
 * Treated exceptions are:<ul>
 * <li><b>NoSessionException</b></li>
 * <li><b>AuthorizationException</b></li>
 * <li><b>InvalidOperationException</b></li>
 * </ul>
 * @author Andrei Cojocaru
 *
 */
public class SimpleErrorMessageResolver extends AErrorMessageResolver {

	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;
	
	/**
	 * Default constructor.
	 */
	public SimpleErrorMessageResolver() {}
	
	/**
	 * Returns a localized message for the passed error.<br/>
	 * @param error
	 * @return
	 */
	protected String getMessage(Throwable error) {
		String msg = null;
		if (error instanceof NoSessionException) {
			msg = messages.exceptionNoSession();
		} else if (error instanceof AuthorizationException) {
			msg = messages.exceptionAuthorization();
		} else if (error instanceof InvalidOperationException) {
			msg = messages.exceptionInvalidOperation();
		} else {
			msg = messages.exceptionUnexpected();
		}
		return msg;
	}
	
	/**
	 * Injects the messages bundle.
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}
	
	/**
	 * @return the generic messages bundle.
	 */
	protected final GuiCommonMessages getMessages() {
		return messages;
	}
}
