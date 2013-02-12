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

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.NoSessionException;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;

/**
 * There are service errors that need more processing than simply displaying an alert window.
 * NoSessionException is such an error, and it may be thrown during the call to any service.
 * This class contains the processing logic for all PMS exceptions, once the particular message
 * is resolved by an {@link IErrorMessageResolver}. All calls to {@link AsyncCallback#onFailure(Throwable)}
 * should delegate to {@link #processError(Throwable, String)}
 * 
 * @author Andrei Cojocaru
 *
 */
public class ServiceErrorsProcessor {
	
	/**
	 * Utilities helper
	 */
	private Util util = null;
	
	/** PMS utilities bundle */
	private PmsUtil pmsUtil = null;
	
	/**
	 * Processes only the error.
	 * @param error
	 * @param message the message to display in the alert box.
	 */
	private void processError(Throwable error, String message) {
		Listener<MessageBoxEvent> callback = null;
		if (error instanceof NoSessionException) { 
			// reload page when exiting alert popup
			callback = new Listener<MessageBoxEvent>() {
				public void handleEvent(MessageBoxEvent be) {
					// Window.Location.reload();
					UrlBuilder url = Window.Location.createUrlBuilder().setHash(null);
					if(pmsUtil.isDisableLogin()){
						int index = url.buildString().lastIndexOf("/");
						Window.Location.replace(url.buildString().substring(0, index));
					} else {
						Window.Location.replace(url.buildString());
					}
				};
			};
		}
		util.error(message, callback);
	}
	
	/**
	 * Processes the message and the error. Once the message is created, shows a modal alert popup
	 * with the complete error message. If the error is a NoSessionException, the browser reloads
	 * the page when popup gets closed.
	 * 
	 * @param error the thrown error.
	 * @param emr the Error Message Resolver maps exceptions on human-readable messages. 
	 * May be <code>null</code>; in this case, the message displayed will be <code>opMessage</code>
	 * @param opMessage message that describes only the operation. Must be passed to the EMR 
	 * 	in order to obtain the complete error message.
	 */
	public void processError(Throwable error, IErrorMessageResolver emr, String opMessage) {
		String message = opMessage;
		if (emr != null) {
			message = emr.getMessage(error, opMessage);
		}
		processError(error, message);
	}
	
	/**
	 * Injects the utilities object 
	 * @param util
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}
	
	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}
	
}
